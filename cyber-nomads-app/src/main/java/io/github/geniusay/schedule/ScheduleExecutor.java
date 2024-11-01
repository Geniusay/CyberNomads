package io.github.geniusay.schedule;

import org.apache.tomcat.util.threads.TaskThreadFactory;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/31 21:36
 */
public class ScheduleExecutor extends ThreadPoolExecutor{

    private final static HashMap<String,SerialExecutor> executorInstances = new HashMap<>(64);

    public ScheduleExecutor(int poolSize,String name) {
        this(poolSize, poolSize, 0, TimeUnit.SECONDS, new SynchronousQueue<>(), new TaskThreadFactory(name,false,Thread.NORM_PRIORITY), new DiscardPolicy());
    }

    public ScheduleExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        if(!(command instanceof RobotTask)){
            throw new RejectedExecutionException("command must be " + RobotTask.class.getName() + "!");
        }
        RobotTask task = (RobotTask)command;
        executorInstances.getOrDefault(task.getId(),new SerialExecutor()).execute(task);
    }

    private void dispatch(Runnable task) {
        super.execute(task);
    }

    private final class SerialExecutor implements Executor,Runnable{

        private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
        public final AtomicInteger state = new AtomicInteger(0);
        @Override
        public void run() {
            if(state.compareAndSet(0,1)){
                try {
                    Thread t = Thread.currentThread();
                    for(;;){
                        final Runnable task = tasks.poll();
                        if (task == null) {
                            break;
                        }
                        task.run();
                    }
                }finally {
                    state.compareAndSet(1,0);
                }

                if (state.get() == 0 && tasks.peek() != null) {
                    dispatch(this);
                }
            }
        }

        @Override
        public void execute(Runnable command) {
            tasks.add(command);
            if(state.compareAndSet(0,1)){
                dispatch(command);
            }
        }
    }

}
