package io.github.geniusay.schedule.storage;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/13 17:05
 */
public interface WorkerStorage {

    void joinRobotWorker(Long workerId);
    void removeRobotWorker(Long workerId);
    void reJoinRobotWorker(Long workerId);
}
