package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.service.RobotService;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IRobotService implements RobotService {

    @Resource
    RobotMapper robotMapper;

    @Override
    public LoadRobotResponseDTO loadRobot(MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        try {
            Map<String, List<UCookie>> cookiesMap = objectMapper.readValue(file.getBytes(), new TypeReference<>() {
            });
            for (Map.Entry<String, List<UCookie>> entry : cookiesMap.entrySet()) {
                String username = entry.getKey();
                List<UCookie> cookies = entry.getValue();
                String cookieString = cookies.stream()
                        .map(uCookie -> uCookie.getName() + "=" + uCookie.getValue())
                        .collect(Collectors.joining(";"));
                RobotDO build = RobotDO.builder()
                        .username(username)
                        .nickname(username)
                        .platform(Platform.BILIBILI.getCode())
                        .cookie(cookieString)
                        .ban(false)
                        .hasDelete(false)
                        .uid(ThreadUtil.getUid())
                        .build();
                try {
                    robotMapper.insert(build);
                    success.add(build.getUsername());
                }catch (Exception e){
                    fail.add(username);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return LoadRobotResponseDTO.builder().successRobot(success).errorRobot(fail).build();
    }

    @Override
    public List<RobotVO> queryRobot() {
        String uid = ThreadUtil.getUid();
        LambdaQueryWrapper<RobotDO> query = new LambdaQueryWrapper<>();
        query.eq(RobotDO::getUid, uid);
        List<RobotDO> robotDOs = robotMapper.selectList(query);
        return robotDOs.stream().map(RobotVO::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean removeRoobot(Long id) {
        String uid = ThreadUtil.getUid();
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).eq("uid", uid).set("has_delete",true))==1;
    }

    @Override
    public Boolean banRoobot(Long id) {
        String uid = ThreadUtil.getUid();
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).eq("uid", uid).set("ban",true))==1;
    }

    @Override
    public Boolean changeRobot(ChangeRobotDTO robotDTO) {
        LambdaUpdateWrapper<RobotDO> update = new LambdaUpdateWrapper<>();
        update.eq(RobotDO::getId, robotDTO.getId())
                .set(RobotDO::getUsername, robotDTO.getUsername())
                .set(RobotDO::getNickname, robotDTO.getNickname())
                .set(RobotDO::getPlatform, robotDTO.getPlatform())
                .set(RobotDO::getCookie, robotDTO.getCookie());
        return robotMapper.update(null, update) == 1;
    }

    @Override
    public List<RobotDO> queryVaildRobot() {
        return robotMapper.selectList(new QueryWrapper<RobotDO>().eq("ban", 0).eq("has_delete", 0));
    }
}
