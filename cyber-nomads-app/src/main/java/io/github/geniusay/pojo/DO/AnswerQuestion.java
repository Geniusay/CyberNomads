package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("answer_question")
public class AnswerQuestion {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String groupTitle;

    private String question;

    private String answer;

    private String englishQuestion;

    private String englishAnswer;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
