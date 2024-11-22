package io.github.geniusay.core.supertask.taskblueprint;

import io.github.geniusay.core.supertask.task.LastWordHandler;
import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskExecute;
import io.github.geniusay.pojo.DO.LastWord;
import io.github.geniusay.utils.LastWordUtil;

import java.util.Map;

public abstract class AbstractTaskBlueprint implements TaskBlueprint {

    public abstract String platform();
    public abstract String taskType();

    public void initBlueprint(Task task) {}

    @Override
    public TaskExecute supplierExecute() {
        return (robot) -> {
            Task task = robot.task();
            try {
                executeTask(robot, task);
            } catch (Exception e) {
                task.addLastWord(robot, null, Map.of("error", e.getMessage()));
            }
            return null;
        };
    }

    @Override
    public LastWordHandler supplierLastWordHandler() {
        return (robot) -> {

            robot.task().getTerminator().doTask(robot);

            LastWord lastWord = robot.task().getLastWord(robot);

            if (lastWord != null) {
                String errorMessage = (String) lastWord.getAdditionalInfo("error");

                if (errorMessage != null) {
                    return LastWordUtil.buildLastWord(String.format("%s robot 执行任务时出现异常: %s", robot.getNickname(), errorMessage), false);
                }

                return lastWord(robot, robot.task(), lastWord);
            }
            return LastWordUtil.buildLastWord(String.format("%s robot 没有生成任何任务结果", robot.getNickname()), false);
        };
    }


    protected abstract void executeTask(RobotWorker robot, Task task) throws Exception;

    protected abstract String lastWord(RobotWorker robot, Task task, LastWord lastWord);
}
