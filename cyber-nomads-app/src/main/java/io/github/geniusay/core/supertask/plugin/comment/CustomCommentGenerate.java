package io.github.geniusay.core.supertask.plugin.comment;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.pojo.DO.RobotDO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.geniusay.constants.PluginConstant.CUSTOM_COMMENT;
import static io.github.geniusay.core.supertask.config.PluginConstant.CUSTOM_COMMENT_GENERATE_PLUGIN;

@Scope("prototype")
@Component(CUSTOM_COMMENT_GENERATE_PLUGIN)
public class CustomCommentGenerate extends AbstractCommentGenerate implements CommentGenerate {

    private final static String COMMENT_SEPARATOR = "###";

    private Map<Long, String> robotCommentMap = new HashMap<>();  // 机器人ID与评论的映射

    // 在init中解析评论，并随机分配给机器人
    @Override
    public void init(Task task) {
        super.init(task);

        // 获取前端传递的customComment，并使用分割符拆分为多个评论
        String customComment = getValue(this.pluginParams, CUSTOM_COMMENT, String.class);
        List<String> comments = Arrays.stream(customComment.split(COMMENT_SEPARATOR))
                .map(String::trim)
                .filter(comment -> !comment.isEmpty())
                .collect(Collectors.toList());

        // 获取机器人列表
        List<RobotDO> robots = task.getRobots();

        // 如果评论数量大于机器人数量，随机丢弃多余的评论
        if (comments.size() > robots.size()) {
            Collections.shuffle(comments);
            comments = comments.subList(0, robots.size());
        }

        // 如果评论数量少于机器人数量，循环分配评论，确保每个机器人都有一条评论
        List<String> assignedComments = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            // 从评论列表中循环取出评论
            String comment = comments.get(i % comments.size());
            assignedComments.add(comment);
        }

        // 随机打乱机器人列表
        Collections.shuffle(robots);

        // 将评论随机分配给机器人
        for (int i = 0; i < robots.size(); i++) {
            robotCommentMap.put(robots.get(i).getId(), assignedComments.get(i));
        }
    }

    // 为每个机器人生成对应的评论
    @Override
    public String generateComment(RobotWorker robot) {
        return robotCommentMap.getOrDefault(robot.getId(), "默认评论");
    }


    @Override
    public String generateComment() {
        return null;
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofK(CUSTOM_COMMENT, String.class, "请编写需要指定的评论，使用 '" + COMMENT_SEPARATOR + "' 分隔每条评论，最多填写机器人数量的评论条数")
                        .setInputType(TaskNeedParams.InputTypeEnum.TEXTAREA)
        );
    }
}
