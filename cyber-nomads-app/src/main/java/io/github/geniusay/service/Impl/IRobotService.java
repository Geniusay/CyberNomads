package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.common.web.Result;
import io.github.geniusay.core.exception.GlobalExceptionHandle;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.Platform;
import io.github.geniusay.pojo.VO.PlatformVO;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.service.RobotService;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.PlatformUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.geniusay.pojo.Platform.BILIBILI;

@Service
public class IRobotService implements RobotService {

    @Resource
    RobotMapper robotMapper;

    @Resource
    TaskMapper taskMapper;

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
                        .platform(BILIBILI.getCode())
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
        query.eq(RobotDO::getUid, uid).eq(RobotDO::isHasDelete,false);
        List<RobotDO> robotDOs = robotMapper.selectList(query);
        return robotDOs.stream().map(RobotVO::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean removeRoobot(Long id) {
        String uid = ThreadUtil.getUid();
        List<TaskDO> taskList = taskMapper.selectList(new QueryWrapper<TaskDO>().eq("uid", uid));
        Set<Long> robotSet = new HashSet<>();
        taskList.forEach(taskDO -> {
            robotSet.addAll(ConvertorUtil.stringToList(taskDO.getRobots()));
        });
        if (robotSet.contains(id)) {
            throw new ServeException(407, "robot已分配");
        }
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).eq("uid", uid).set("has_delete",true))==1;
    }

    @Override
    public Boolean banRoobot(Long id) {
        String uid = ThreadUtil.getUid();
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).eq("uid", uid).set("ban",true))==1;
    }

    @Override
    public Boolean changeRobot(ChangeRobotDTO robotDTO) {
        PlatformUtil.checkPlatform(robotDTO.getPlatform());

        LambdaUpdateWrapper<RobotDO> update = new LambdaUpdateWrapper<>();
        update.eq(RobotDO::getId, robotDTO.getId())
                .eq(RobotDO::getUid, ThreadUtil.getUid())
                .set(RobotDO::getUsername, robotDTO.getUsername())
                .set(RobotDO::getNickname, robotDTO.getNickname())
                .set(RobotDO::getPlatform, robotDTO.getPlatform());
        return robotMapper.update(null, update) == 1;
    }

    @Override
    public Result<?> changeRobotCookie(UpdateCookieDTO updateCookieDTO) {
        LambdaUpdateWrapper<RobotDO> update = new LambdaUpdateWrapper<>();
        update.eq(RobotDO::getId, updateCookieDTO.getId())
                .eq(RobotDO::getUid, ThreadUtil.getUid())
                .set(RobotDO::getCookie, updateCookieDTO.getCookie());
        if (robotMapper.update(null, update) != 1) {
            throw new ServeException(400,"更新cookie失败");
        }
        return Result.success();
    }

    @Override
    public List<RobotDO> queryVaildRobot() {
        return robotMapper.selectList(new QueryWrapper<RobotDO>().eq("ban", 0).eq("has_delete", 0));
    }

    @Override
    public Boolean addRobot(AddRobotDTO robotDTO) {
        PlatformUtil.checkPlatform(robotDTO.getPlatform());

        return robotMapper.insert(
                RobotDO.builder()
                    .username(robotDTO.getUsername())
                    .cookie(robotDTO.getCookie())
                    .platform(robotDTO.getPlatform())
                    .nickname(robotDTO.getNickname())
                    .uid(ThreadUtil.getUid())
                    .build()
        ) == 1;
    }

    @Override
    public Map<String, String> getCookie(GetCookieDTO getCookieDTO) {
        QueryWrapper<RobotDO> query = new QueryWrapper<RobotDO>()
                .eq("uid", ThreadUtil.getUid())
                .eq("id", getCookieDTO.getId())
                .select("cookie");
        try {
            return Map.of("cookie", Optional.ofNullable(robotMapper.selectOne(query)).map(RobotDO::getCookie).orElse(""));
        } catch (Exception e) {
            throw new ServeException(409, "robot不存在");
        }
    }

    @Override
    public Boolean insertOrUpdateRobot(LoginMachineDTO loginMachineDTO) {
        int code = PlatformUtil.convertStringToCode(loginMachineDTO.getPlatform());
        Integer res = robotMapper.insertOrUpdate(loginMachineDTO.getUsername(), code, loginMachineDTO.getCookie(),ThreadUtil.getUid());
        return res==1||res==2;
    }
}
