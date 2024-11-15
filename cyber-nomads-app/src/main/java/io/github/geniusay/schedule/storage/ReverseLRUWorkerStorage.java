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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
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
    private Node highSpeedHead;
    private Node highSpeedTail;
    private Node midSpeedHead;
    private Node midSpeedTail;
    private final ReentrantLock highSpeedLock = new ReentrantLock();
    private final ReentrantLock midSpeedLock = new ReentrantLock();
    private final ReentrantLock slowSpeedLock = new ReentrantLock();
    private final Lock queueLock = new ReentrantLock();
    private final Condition condition = queueLock.newCondition();
    private boolean highSpeedConditionFlag = false;
    private boolean midSpeedConditionFlag = false;
    private boolean slowSpeedConditionFlag = false;
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    public void init(){
        initHighQueue();
        initMidQueue();
        executor.scheduleWithFixedDelay(() -> {
            try {
                checkWorkerDown();
                checkWorkerUp();
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 1, 1000, TimeUnit.MILLISECONDS);
        new Thread(
                this::notifyQueue
        ).start();
    }
    private void checkWorkerDown(){
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
    }
    private void checkWorkerUp(){
        if(!midSpeedQueue.isEmpty()){
            midSpeedQueue.forEach((id,node)->{
                if(!checkRobotFIFO(manager.getRobotTaskById(id).size())){
                    midSpeedLock.lock();
                    try {
                        createHighSpeedNode(id);
                        midSpeedQueue.remove(id);
                    }finally {
                        midSpeedLock.unlock();
                    }
                }
            });
        }
    }
    private void notifyQueue(){
        queueLock.lock();
        try {
            while (true){
                while (highSpeedConditionFlag){
                    selectHighSpeedWorker();
                }
                if(midSpeedConditionFlag){
                    selectMidSpeedWorker();
                }else if(slowSpeedConditionFlag){
                    selectSlowSpeedWorker();
                }else{
                    condition.await();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            queueLock.unlock();
        }
    }
    @Override
    public void doHandle(Long workerId) {
        Node node;
        if(checkRobotFIFO(manager.getRobotTaskById(workerId).size())){
            node = createMidSpeedNode(workerId);
        }else{
            node = createHighSpeedNode(workerId);
        }
        notifyHighSpeedWorker();
    }
    private void notifyHighSpeedWorker(){
        queueLock.lock();
        try {
            highSpeedConditionFlag=true;
            condition.signal();
        }finally {
            queueLock.unlock();
        }
    }
    private void notifyMidSpeedWorker(){
        queueLock.lock();
        if(highSpeedQueue.isEmpty()){
            highSpeedConditionFlag = false;
            midSpeedConditionFlag = true;
            condition.signal();
        }
        queueLock.unlock();
    }
    private void notifySlowSpeedWorker(){
        queueLock.lock();
        try {
            if(midSpeedQueue.isEmpty()){
                slowSpeedConditionFlag=true;
                condition.signal();
            }
        }finally {
            queueLock.unlock();
        }
    }
    private void selectHighSpeedWorker(){
        log.info("拉取l1缓存robot");
        highSpeedLock.lock();
        try {
            if(!highSpeedQueue.isEmpty()){
                Node e = popHighSpeedFirstElement();
                if(e==null){
                    return;
                }
                pushRobot(e.workerId);
            }
        }finally {
            highSpeedLock.unlock();
        }
        notifyMidSpeedWorker();
    }
    private void selectMidSpeedWorker(){
        log.info("拉取l2缓存robot");
        midSpeedLock.lock();
        try {
            if(!midSpeedQueue.isEmpty()){
                Node e = popMidSpeedFirstElement();
                if(e==null){
                    return;
                }
                pushRobot(e.workerId);
            }
        }finally {
            midSpeedLock.unlock();
        }
        notifySlowSpeedWorker();
        midSpeedConditionFlag=false;
    }
    private void selectSlowSpeedWorker(){
        log.info("拉取l3缓存robot");
        slowSpeedLock.lock();
        try {
            if(!slowSpeedQueue.isEmpty()){
                Long workerId = slowSpeedQueue.keySet().iterator().next();
                slowSpeedQueue.remove(workerId);
                pushRobot(workerId);
            }
        }finally {
            slowSpeedLock.unlock();
        }
        slowSpeedConditionFlag=false;
    }
    private Node createHighSpeedNode(Long workerId) {
        Node curNode;
        highSpeedLock.lock();
        try {
            curNode = highSpeedQueue.computeIfAbsent(workerId, id -> createNode(workerId));
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
            curNode = midSpeedQueue.computeIfAbsent(workerId, id -> createNode(workerId));
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
        Node node = slowSpeedQueue.computeIfAbsent(workerId, id -> createNode(workerId));
        node.count.incrementAndGet();
        slowSpeedLock.lock();
        try {
            if(node.count.get()<5){
                log.info("创建l1缓存robot");
                createHighSpeedNode(workerId);
                notifyHighSpeedWorker();
            }
        }finally {
            slowSpeedLock.unlock();
        }
    }

    @Override
    public void afterTaskDone(Long workerId) {
        slowSpeedQueue.remove(workerId);
    }

    private Boolean checkRobotFIFO(Integer num) {
        int totalTask = manager.getWorldTask().size();
        return totalTask>100 && num >= totalTask / 5;
    }

    private void pushRobot(Long workerId){
        eventManager.publishEvent(new robotSelecteEvent(workerId));
    }

    private void initHighQueue(){
        highSpeedTail = new Node();
        highSpeedHead = new Node();
        highSpeedHead.next = highSpeedTail;
        highSpeedTail.pre = highSpeedHead;
    }
    private void initMidQueue() {
        midSpeedHead = new Node();
        midSpeedTail = new Node();
        midSpeedHead.next = midSpeedTail;
        midSpeedTail.pre = midSpeedHead;
    }
    private Node createNode(Long workerId){
        Node node = new Node();
        node.setWorkerId(workerId);
        node.count = new AtomicInteger(0);
        return node;
    }
    @Data
    static class Node{
        private Long workerId;
        private Node next;
        private Node pre;
        private AtomicInteger count;
    }
}
