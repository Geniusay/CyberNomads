package io.github.geniusay.schedule.strategy;

import io.github.geniusay.schedule.TaskScheduleManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/10 16:05
 */
@Component
public class ReverseLRUWorkerExecute extends AbstractWorkerExecute {
    @Resource
    TaskScheduleManager manager;
    Map<Long,Node> highSpeedMap = new ConcurrentHashMap<>();
    Map<Long,Node> midSpeedMap = new ConcurrentHashMap<>();
    private Node highSpeedHead;
    private Node highSpeedTail;
    private Node midSpeedHead;
    private Node midSpeedTail;

    private final ReentrantLock highSpeedLock = new ReentrantLock();
    private final ReentrantLock midSpeedLock = new ReentrantLock();

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);

    @PostConstruct
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
                if(!highSpeedMap.isEmpty()){
                    //todo: 是否需要锁
                    highSpeedMap.forEach((id,node)->{
                        if(checkRobotFIFO(robotWorkingCount.get(id))){
                            createMidSpeedNode(id);
                            highSpeedMap.remove(id);
                        }
                    });
                }
                if(!midSpeedMap.isEmpty()){
                    midSpeedMap.forEach((id,node)->{
                        if(!checkRobotFIFO(robotWorkingCount.get(id))){
                            createHighSpeedNode(id);
                            midSpeedMap.remove(id);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);

        executor.scheduleWithFixedDelay(()->{
            if(!highSpeedMap.isEmpty()){
                Long workerId = getHighSpeedFirstElement();

            }
        },2,100,TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(()->{
            if(!midSpeedMap.isEmpty()){
                Long workerId = getMidSpeedFirstElement();

            }
        },2,1000,TimeUnit.MILLISECONDS);
    }

    @Override
    public void doHandle(Long workerId) {
        if(checkRobotFIFO(robotWorkingCount.get(workerId))){
            createMidSpeedNode(workerId);
        }else{
            createHighSpeedNode(workerId);
        }
    }
    private void createHighSpeedNode(Long workerId) {
        Node curNode = highSpeedMap.computeIfAbsent(workerId, id -> {
            Node node = new Node();
            node.setWorkerId(workerId);
            return node;
        });

        Node preTail = highSpeedTail.pre;
        preTail.next = curNode;
        curNode.pre = preTail;
        curNode.next = highSpeedTail;
        highSpeedTail.pre = curNode;
    }

    private void createMidSpeedNode(Long workerId){
        Node curNode = midSpeedMap.computeIfAbsent(workerId, id -> {
            Node node = new Node();
            node.setWorkerId(workerId);
            return node;
        });

        Node preTail = midSpeedTail.pre;
        preTail.next = curNode;
        curNode.pre = preTail;
        curNode.next = midSpeedTail;
        midSpeedTail.pre = curNode;
    }

    public Long getHighSpeedFirstElement() {
        Node first = highSpeedHead.next;
        if(first==highSpeedTail)
            return null;
        highSpeedHead.next = first.next;
        first.next.pre = highSpeedHead;
        return first.workerId;
    }

    public Long getMidSpeedFirstElement(){
        Node first = midSpeedHead.next;
        if(first==midSpeedTail)
            return null;
        midSpeedHead.next = first.next;
        first.next.pre = midSpeedHead;
        return first.workerId;
    }

    public void removeElement(String workerId){
        Node remove = highSpeedMap.remove(workerId);
        remove.pre.next = remove.next;
        remove.next.pre = remove.pre;
    }

    @Data
    static class Node{
        private Long workerId;
        private Node next;
        private Node pre;
    }

    private Boolean checkRobotFIFO(Integer num){
        int totalTask = manager.getWorldTask().size();
        return num>=totalTask/5;
    }
}
