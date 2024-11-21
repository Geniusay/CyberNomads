package io.github.geniusay.schedule.storage;

import io.github.geniusay.core.event.EventManager;
import io.github.geniusay.core.event.commonEvent.robotSelecteEvent;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/10 16:05
 */
@Component
@Slf4j
public class ReverseLRUWorkerStorage extends AbstractWorkerStorage {
    @Resource
    TaskScheduleManager manager;
    @Resource
    EventManager eventManager;

    private final BlockingQueue<Long> highQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Long> midQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Long> slowQueue = new LinkedBlockingQueue<>();
    private final Map<Long,Integer> retry = new ConcurrentHashMap<>();
    private final int highInterval = 100;
    private final int midInterval = 1000;
    private final int slowInterval = 5000;
    @Override
    public void joinWorkerQueue(Long workerId) {
        choiceWorkerQueue(workerId);
    }

    @Override
    public void reJoinRobotWorker(Long workerId) {
        Integer count = retry.compute(workerId, (key, value) -> {
            if (value == null) {
                return 1;
            } else {
                return value + 1;
            }
        });
        if(count<5){
            highQueue.add(workerId);
        }else{
            slowQueue.add(workerId);
        }
    }

    @Override
    public void taskDoneCallBack(Long workerId) {
        retry.remove(workerId);
    }

    private void choiceWorkerQueue(Long workerId){
        int totalTask = manager.getWorldTask().size();
        int workerTask = manager.getRobotTaskById(workerId).size();
        if(totalTask/5>workerTask||totalTask<100){
            highQueue.add(workerId);
        }else{
            midQueue.add(workerId);
        }
    }

    @Override
    public void init() {
       new Thread(this::notifyQueue).start();
    }

    private void notifyQueue(){
        long lastHigh = System.currentTimeMillis();
        long lastMid = System.currentTimeMillis();
        long lastSlow = System.currentTimeMillis();
        try {
            while (true){
                long current = System.currentTimeMillis();
                if(current-lastHigh>=highInterval&&!highQueue.isEmpty()){
                    Long workerId = highQueue.take();
                    pushWorker(workerId);
                    lastHigh = current;
                }
                if(current-lastMid>=midInterval&&!midQueue.isEmpty()){
                    Long workerId = midQueue.take();
                    pushWorker(workerId);
                    lastMid=current;
                }
                if(current-lastSlow>=slowInterval&&!slowQueue.isEmpty()){
                    Long workerId = slowQueue.take();
                    pushWorker(workerId);
                    lastSlow=current;
                }
                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void pushWorker(Long workerId){
        eventManager.publishEvent(new robotSelecteEvent(workerId));
    }
}
