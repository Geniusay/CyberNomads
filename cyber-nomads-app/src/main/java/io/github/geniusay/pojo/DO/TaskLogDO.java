package io.github.geniusay.pojo.DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("task_log")
@NoArgsConstructor
@Data
public class TaskLogDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                 // 日志ID
    private boolean success;         // 是否成功
    private String errorCode;        // 错误状态码
    private String errorMessage;     // 错误信息
    private String executionLog;     // 执行过程（1.XXX，2.XXX）
    private LocalDateTime startTime;  // 任务开始时间
    private LocalDateTime endTime;    // 任务结束时间

    public TaskLogDO(boolean success, LocalDateTime startTime, LocalDateTime endTime, String errorCode, String errorMessage, String executionLog) {
        this.success = success;
        this.startTime = startTime;
        this.endTime = endTime;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.executionLog = executionLog;
    }
}
