package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.UserDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:00
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select r.* from robot r inner join user_robot ur on r.id = ur.robot_id where ur.uid = #{uid} and r.ban = 0 and r.has_delete = 0")
    List<RobotDO> queryRobotsByUid(String uid);

    @Delete("delete from user_robot where uid = #{uid} and robot_id = #{robotId}")
    Integer delRobot(String uid,Long robotId);

    @Insert("INSERT INTO user_robot (uid, robot_id) VALUES (#{uid}, #{robotId})")
    Integer insertUserRobot(String uid,Long robotId);
}
