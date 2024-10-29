package io.github.geniusay.pojo.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@TableName("point_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Long recordId;

    private String uid;

    private String pointOption;

    private Integer point;
}
