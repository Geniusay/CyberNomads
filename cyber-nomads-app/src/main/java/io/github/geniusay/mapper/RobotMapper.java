package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 12:13
 */
@Mapper
public interface RobotMapper extends BaseMapper<RobotDO> {

    @Select("<script>" +
            "SELECT COUNT(*) > 0 " +
            "FROM robot_table " +
            "WHERE uid = #{uid} " +
            "AND platform = #{platform} " +
            "AND robot_id IN ( " +
            "    <foreach collection='robotDOList' item='robotId' separator=',' open='(' close=')'>" +
            "        #{robotId} " +
            "    </foreach> " +
            ") " +
            "</script>")
    Boolean queryRobotValid(List<Long> robotDOList,String uid,Integer platform);

    @Update("update robot set cookie = #{cookie}, update_time= #{updateTime} WHERE uid = #{uid} AND username = #{username} AND ban = 0 AND has_delete=0")
    Integer updateRobot(String username,String cookie,String uid,String updateTime);
}
