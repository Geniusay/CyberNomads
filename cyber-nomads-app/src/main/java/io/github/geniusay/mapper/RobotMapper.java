package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Insert("INSERT INTO robot (username, nickname, platform, cookie, uid, ban, has_delete, create_time, update_time) " +
            "VALUES (#{username}, #{username}, #{platform}, #{cookie}, #{uid}, 0, 0, NOW(), NOW()) " +
            "ON DUPLICATE KEY UPDATE cookie = #{cookie}, update_time = NOW()")
    int insertOrUpdate(@Param("username") String username,
                       @Param("platform") Integer platform,
                       @Param("cookie") String cookie,
                       @Param("uid") String uid);

}
