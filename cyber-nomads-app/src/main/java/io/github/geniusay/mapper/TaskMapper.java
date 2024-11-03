package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.geniusay.pojo.DO.TaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TaskMapper extends BaseMapper<TaskDO> {

    /**
     * 通过任务 ID 和用户 ID 查询任务
     */
    @Select("SELECT * FROM task WHERE id = #{taskId} AND uid = #{uid}")
    TaskDO selectTaskByIdAndUid(@Param("taskId") Long taskId, @Param("uid") String uid);
}