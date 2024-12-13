package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import io.github.geniusay.pojo.VO.SharedRobotVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SharedRobotMapper extends BaseMapper<SharedRobotDO> {
    @Select("SELECT r.id, r.platform, r.nickname, r.username, r.ban, r.has_delete, r.uid, sr.focus_task\n" +
            "FROM robot r\n" +
            "JOIN (\n" +
            "    SELECT robot_id, focus_task\n" +
            "    FROM shared_robot\n" +
            "    ORDER BY robot_id\n" +
            "    LIMIT 1\n" +
            ") AS sr ON r.id = sr.robot_id")
    SharedRobotVO selectSharedDataById(@Param("id") Long id);

    @Select("SELECT r.id, r.platform, r.nickname, r.username, r.ban, r.has_delete, r.uid, sr.focus_task\n" +
            "FROM robot r\n" +
            "JOIN (\n" +
            "    SELECT robot_id, focus_task\n" +
            "    FROM shared_robot\n" +
            "    ORDER BY robot_id\n" +
            ") AS sr ON r.id = sr.robot_id")
    Page<SharedRobotVO> selectSharedData(Page<SharedRobotDO> page);

    @Select("SELECT r.id, r.platform, r.nickname, r.username, r.cookie, r.ban, r.has_delete, r.uid, sr.focus_task\n" +
            "FROM robot r\n" +
            "JOIN (\n" +
            "    SELECT robot_id, focus_task\n" +
            "    FROM shared_robot\n" +
            "    WHERE focus_task LIKE \',video:comment:,\'\n" +
            "    ORDER BY robot_id\n" +
            ") AS sr ON r.id = sr.robot_id;")
    Page<SharedRobotVO> selectSharedData(@Param("taskType")String taskType, Page<SharedRobotDO> page);
}