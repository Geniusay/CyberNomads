package io.github.geniusay.core.supertask.task;

import io.github.geniusay.pojo.DO.RobotDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotWorker {

    private Long id;
    private String nickname;
    private String username;
    private String cookie;

    private Task currentTask;

    public RobotWorker(RobotDO robotDO) {
        this.id = robotDO.getId();
        this.nickname = robotDO.getNickname();
        this.username = robotDO.getUsername();
        this.cookie = robotDO.getCookie();
    }

    public Task task() {
        return currentTask;
    }
}
