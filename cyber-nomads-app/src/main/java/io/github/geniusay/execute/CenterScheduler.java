package io.github.geniusay.execute;

import io.github.geniusay.core.supertask.TaskStrategyManager;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/31 21:23
 */
public class CenterScheduler extends ThreadPoolExecutor {

    private static final AtomicIntegerFieldUpdater<workerExecutor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(workerExecutor.class, "state");
    private static HashMap<Long, workerExecutor> worldTaskMap = new HashMap<>();
    private static volatile Set<Long> freeRobots = new HashSet<>();
    private static volatile Set<Long> workingRobots = new HashSet<>();

    public CenterScheduler(int poolSize,String name) {
        this(poolSize, poolSize, 0, TimeUnit.SECONDS, new SynchronousQueue<>(), new TaskThreadFactory(name,false,Thread.NORM_PRIORITY), new DiscardPolicy());
    }

    public CenterScheduler(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        if(!(command instanceof RobotTask)){
            throw new RuntimeException("command must be " + RobotTask.class.getName() + "!");
        }
        RobotTask robotTask = (RobotTask) command;
        worldTaskMap.getOrDefault(robotTask.getWorker().getId(),new workerExecutor(robotTask.getWorker())).execute(command);
    }

    public static void remove(Long robotId, String taskId){

    }
    private void dispatch(Runnable task) {
        super.execute(task);
    }
    private final class workerExecutor implements Executor,Runnable{

        private RobotWorker worker;
        private final Queue<Runnable> tasks;
        public volatile int state;

        public volatile boolean ban;
        public workerExecutor(RobotWorker worker){
            this.worker = worker;
            this.tasks = new ConcurrentLinkedQueue<>();
        }
        @Override
        public void run() {
            if(STATE_UPDATER.compareAndSet(this, 0, 1)&&!ban){
                try {
                    freeRobots.remove(this.worker.getId());
                    workingRobots.add(this.worker.getId());
                    for(;;){
                        final Runnable task = tasks.poll();
                        if(task==null){
                            break;
                        }
                        task.run();
                    }
                }finally {
                    STATE_UPDATER.set(this, 0);
                    workingRobots.remove(this.worker.getId());
                    freeRobots.add(this.worker.getId());
                }
                if (STATE_UPDATER.get(this) == 0 && tasks.peek() != null && !ban) {
                    dispatch(this);
                }
            }
        }

        @Override
        public void execute(Runnable command) {
            tasks.add(command);
            if (STATE_UPDATER.get(this) == 0) {
                dispatch(this);
            }
        }
    }
}
