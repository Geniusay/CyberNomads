package io.github.geniusay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.geniusay.pojo.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 22:00
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDTO> {
}
