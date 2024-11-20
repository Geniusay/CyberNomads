package io.github.geniusay.schedule.storage;

import io.github.geniusay.core.event.EventManager;
import io.github.geniusay.core.event.commonEvent.robotSelecteEvent;
import io.github.geniusay.schedule.TaskScheduleManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
    Map<Long,Node> highSpeedQueue = new ConcurrentHashMap<>();
    Map<Long,Node> midSpeedQueue = new ConcurrentHashMap<>();
    Map<Long,Node> slowSpeedQueue = new LinkedHashMap<>();
    Map<Long,Node> workerCache = new ConcurrentHashMap<>();
    private Node highSpeedHead;
    private Node highSpeedTail;
    private Node midSpeedHead;
    private Node midSpeedTail;

    private final ReentrantLock highSpeedLock = new ReentrantLock();
    private final ReentrantLock midSpeedLock = new ReentrantLock();
    private final ReentrantLock slowSpeedLock = new ReentrantLock();
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

    public void init(){
        highSpeedTail = new Node();
        highSpeedHead = new Node();
        highSpeedHead.next = highSpeedTail;
        highSpeedTail.pre = highSpeedHead;
        midSpeedHead = new Node();
        midSpeedTail = new Node();
        midSpeedHead.next = midSpeedTail;
        midSpeedTail.pre = midSpeedHead;

        executor.scheduleWithFixedDelay(() -> {
            try {
                if(!highSpeedQueue.isEmpty()){
                    highSpeedQueue.forEach((id,node)->{
                        if(checkRobotFIFO(manager.getRobotTaskById(id).size())){
                            highSpeedLock.lock();
                            try {
                                createMidSpeedNode(id);
                                highSpeedQueue.remove(id);
                            }finally {
                                highSpeedLock.unlock();
                            }
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 1, 1000, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(() -> {

            try {
                if(!midSpeedQueue.isEmpty()){
                    midSpeedQueue.forEach((id,node)->{
                        if(!checkRobotFIFO(manager.getRobotTaskById(id).size())){
                            createHighSpeedNode(id);
                            midSpeedQueue.remove(id);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {

            }
        }, 1, 2000, TimeUnit.MILLISECONDS);

        executor.scheduleWithFixedDelay(()->{
            try {
                if(!highSpeedQueue.isEmpty()){
                    Node e = popHighSpeedFirstElement();
                    if(e==null){
                        return;
                    }
                    pushRobot(e.workerId);
                }
            }finally {

            }
        },1,100,TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(()->{
            try {
                if(!midSpeedQueue.isEmpty()){
                    Node e = popMidSpeedFirstElement();
                    if(e==null){
                        return;
                    }
                    pushRobot(e.workerId);
                }
            }finally {

            }
        },1,1000,TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(()->{
            workerCache.forEach((k,v)->{
                if(!highSpeedQueue.containsKey(k)&&!midSpeedQueue.containsKey(k)&&!slowSpeedQueue.containsKey(k)){
                    workerCache.remove(k);
                }
            });
            slowSpeedLock.lock();
            try {
                if(slowSpeedQueue.isEmpty()){
                    return;
                }
                Long workerId = slowSpeedQueue.keySet().iterator().next();
                slowSpeedQueue.remove(workerId);
                pushRobot(workerId);
            }finally {
                slowSpeedLock.unlock();
            }
        },1,5000,TimeUnit.MILLISECONDS);
    }

    @Override
    public void joinWorkerQueue(Long workerId) {
        if(highSpeedQueue.containsKey(workerId)||midSpeedQueue.containsKey(workerId)){
            if(slowSpeedQueue.containsKey(workerId)){
                slowSpeedLock.lock();
                highSpeedLock.lock();
                try {
                    highSpeedQueue.put(workerId,slowSpeedQueue.get(workerId));
                    slowSpeedQueue.remove(workerId);
                }finally {
                    slowSpeedLock.unlock();
                    highSpeedLock.unlock();
                }
            }
            return;
        }
        Node node;
        if(checkRobotFIFO(manager.getRobotTaskById(workerId).size())){
            node = createMidSpeedNode(workerId);
        }else{
            node = createHighSpeedNode(workerId);
        }
        if(node!=null){
            workerCache.put(workerId,node);
        }
    }

    private Node createHighSpeedNode(Long workerId) {
        Node curNode;
        highSpeedLock.lock();
        try {
            curNode = highSpeedQueue.computeIfAbsent(workerId, id -> {
                Node node = new Node();
                node.setWorkerId(workerId);
                node.count = new AtomicInteger(0);
                return node;
            });
            addNodeToTail(highSpeedTail,curNode);
        } finally {
            highSpeedLock.unlock();
        }
        return curNode;
    }

    private Node createMidSpeedNode(Long workerId){
        midSpeedLock.lock();
        Node curNode;
        try {
                curNode = midSpeedQueue.computeIfAbsent(workerId, id -> {
                Node node = new Node();
                node.setWorkerId(workerId);
                node.count = new AtomicInteger(0);
                return node;
            });
            addNodeToTail(midSpeedTail,curNode);
        }finally {
            midSpeedLock.unlock();
        }
        return curNode;
    }

    private void addNodeToTail(Node tail,Node cur){
        Node preTail = tail.pre;
        preTail.next = cur;
        cur.pre = preTail;
        cur.next = tail;
        tail.pre = cur;
    }

    public Node popHighSpeedFirstElement() {
        Node first = highSpeedHead.next;
        if(first==highSpeedTail)
            return null;
        highSpeedHead.next = first.next;
        first.next.pre = highSpeedHead;
        highSpeedQueue.remove(first.workerId);
        return first;
    }

    public Node popMidSpeedFirstElement(){
        Node first = midSpeedHead.next;
        if(first==midSpeedTail)
            return null;
        midSpeedHead.next = first.next;
        first.next.pre = midSpeedHead;
        midSpeedQueue.remove(first.getWorkerId());
        return first;
    }

    public void removeElement(String workerId){
        Node remove = highSpeedQueue.remove(workerId);
        remove.pre.next = remove.next;
        remove.next.pre = remove.pre;
    }

    @Override
    public void reJoinRobotWorker(Long workerId) {
        Node node = workerCache.get(workerId);
        if(node!=null){
            AtomicInteger count = node.getCount();
            if(count.get()>5){
                slowSpeedQueue.putIfAbsent(workerId,node);
            }else {
                count.incrementAndGet();
                if(checkRobotFIFO(manager.getRobotTaskById(workerId).size())){
                    createMidSpeedNode(workerId);
                }else{
                    createHighSpeedNode(workerId);
                }
            }
        }
    }

    @Data
    static class Node{
        private Long workerId;
        private Node next;
        private Node pre;
        private AtomicInteger count;
    }

    private Boolean checkRobotFIFO(Integer num) {
        int totalTask = manager.getWorldTask().size();
        return totalTask>50 && num >= totalTask / 5;
    }

    private void pushRobot(Long workerId){
        eventManager.publishEvent(new robotSelecteEvent(workerId));
    }
}
