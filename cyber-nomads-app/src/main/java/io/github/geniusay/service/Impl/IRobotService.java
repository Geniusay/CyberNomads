package io.github.geniusay.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.common.web.Result;
import io.github.geniusay.constants.RCode;
import io.github.geniusay.core.cache.SharedRobotCache;
import io.github.geniusay.core.event.EventManager;
import io.github.geniusay.core.exception.ServeException;
import io.github.geniusay.core.supertask.config.TaskTypeConstant;
import io.github.geniusay.mapper.RobotMapper;
import io.github.geniusay.mapper.SharedRobotMapper;
import io.github.geniusay.mapper.TaskMapper;
import io.github.geniusay.pojo.DO.RobotDO;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import io.github.geniusay.pojo.DO.TaskDO;
import io.github.geniusay.pojo.DTO.*;
import io.github.geniusay.pojo.VO.RobotVO;
import io.github.geniusay.pojo.VO.SharedRobotVO;
import io.github.geniusay.service.QrCodeLogin;
import io.github.geniusay.service.RobotService;
import io.github.geniusay.utils.ConvertorUtil;
import io.github.geniusay.utils.DateUtil;
import io.github.geniusay.utils.PlatformUtil;
import io.github.geniusay.utils.ThreadUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.geniusay.pojo.Platform.BILIBILI;
import static io.github.geniusay.utils.ValidUtil.isValidConstant;

@Service
public class IRobotService implements RobotService {

    @Resource
    RobotMapper robotMapper;

    @Resource
    TaskMapper taskMapper;

    @Resource
    private SharedRobotMapper sharedRobotMapper;

    @Resource
    private SharedRobotCache sharedRobotCache;

    @Resource
    private EventManager eventManager;

    @Resource
    private QrCodeLogin qrCodeLogin;

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
        return robotDOs.stream().map(robotDO -> RobotVO.convert(robotDO, sharedRobotCache.exist(robotDO.getId()))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean removeRoobot(Long id) {
        // 判断robot是否处于共享状态
        if(isSharedRobot(id)){
            throw new ServeException(RCode.ROBOT_IN_SHARED);
        }
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
        // 判断robot是否处于共享状态
        if(isSharedRobot(id)){
            throw new ServeException(RCode.ROBOT_IN_SHARED);
        }
        String uid = ThreadUtil.getUid();
        return robotMapper.update(null,new UpdateWrapper<RobotDO>().eq("id",id).eq("uid", uid).set("ban",true))==1;
    }

    // 用于判断账号是否正在共享
    private Boolean isSharedRobot(Long id) {
        return sharedRobotCache.exist(id);
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
        if(checkCookie(updateCookieDTO.getCookie())){
            LambdaUpdateWrapper<RobotDO> update = new LambdaUpdateWrapper<>();
            update.eq(RobotDO::getId, updateCookieDTO.getId())
                    .eq(RobotDO::getUid, ThreadUtil.getUid())
                    .set(RobotDO::getCookie, updateCookieDTO.getCookie());
            if (robotMapper.update(null, update) != 1) {
                throw new ServeException(400,"更新cookie失败");
            }
            return Result.success();
        }
        throw new ServeException(400,"更新cookie失败");
    }

    @Override
    public List<RobotDO> queryVaildRobot() {
        return robotMapper.selectList(new QueryWrapper<RobotDO>().eq("ban", 0).eq("has_delete", 0));
    }

    @Override
    public Boolean addRobot(AddRobotDTO robotDTO) {
        PlatformUtil.checkPlatform(robotDTO.getPlatform());

        try {
            return robotMapper.insert(
                    RobotDO.builder()
                            .username(robotDTO.getUsername())
                            .cookie(robotDTO.getCookie())
                            .platform(robotDTO.getPlatform())
                            .nickname(robotDTO.getNickname())
                            .uid(ThreadUtil.getUid())
                            .build()
            ) == 1;
        } catch (DuplicateKeyException e){
            // 已有robot
            LambdaUpdateWrapper<RobotDO> update = new LambdaUpdateWrapper<>();
            update.eq(RobotDO::getUid, ThreadUtil.getUid())
                    .eq(RobotDO::getUsername, robotDTO.getUsername())
                    .eq(RobotDO::isHasDelete, true)
                    .set(RobotDO::getNickname, robotDTO.getNickname())
                    .set(RobotDO::getPlatform, robotDTO.getPlatform())
                    .set(RobotDO::getCookie, robotDTO.getCookie())
                    .set(RobotDO::isHasDelete, false);
            return robotMapper.update(null, update) == 1;
        }
    }

    /**
     * 二维码扫码登录并且添加机器人
     * @param robotDTO
     * @return 是否添加
     */
    @Override
    public Boolean addRobotQr(AddRobotDTO robotDTO) {
        try {
            // 用cookie字段代表的key去进行验证得到cookie并进行替换
            robotDTO.setCookie(qrCodeLogin.getCookieByKey(robotDTO.getCookie()));
            // 调用添加机器人接口
            return addRobot(robotDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        String uid = ThreadUtil.getUid();
        RobotDO robotDO = robotMapper.selectOne(new QueryWrapper<RobotDO>().eq("uid", uid).eq("username", loginMachineDTO.getUsername()));
        if(robotDO==null){
            AddRobotDTO robotdo = AddRobotDTO.builder().username(loginMachineDTO.getUsername()).platform(code).cookie(loginMachineDTO.getCookie()).nickname(loginMachineDTO.getUsername()).build();
            this.addRobot(robotdo);
        }else{
            robotMapper.updateRobot(robotDO.getUsername(),loginMachineDTO.getCookie(),uid,DateUtil.formatTimestamp(System.currentTimeMillis() / 1000));
        }
        return true;
    }

    @Override
    public Boolean shareRobot(ShareRobotDTO shareRobotDTO) {
        /*
        * 缓存更新策略选择
        * 1、通过缓存更新，后续从缓存批量同步到数据库（可能出现情况，如果缓存未及时同步到数据库停止项目，可能会导致数据的丢失）
        * 2、直接更新到数据库，并且更新缓存（可能出现缓存与数据库不一致的问题）
        * */
        checkTaskTypes(shareRobotDTO.getFocusTask());
        String uid = ThreadUtil.getUid();
        LambdaQueryWrapper<RobotDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RobotDO::getUid, uid)
                .eq(RobotDO::getId, shareRobotDTO.getRobotId())
                .eq(RobotDO::isHasDelete, false)
                .eq(RobotDO::isBan, false);
        if(shareRobotDTO.getFlag()){
            // 添加共享账号
            try {
                RobotDO robotDO = robotMapper.selectOne(wrapper);
                SharedRobotDO sharedRobotDO = SharedRobotDO.builder()
                        .nickname(robotDO.getNickname())
                        .sharedTime(new Date())
                        .robotId(robotDO.getId())
                        .focusTask(listToString(shareRobotDTO.getFocusTask()))
                        .qualificationRate(1.0f)
                        .build();
                return (sharedRobotMapper.insert(sharedRobotDO) == 1) && (sharedRobotCache.putSharedRobot(sharedRobotDO));
            } catch (Exception e){
                throw new ServeException(RCode.SHARE_FAILED);
            }
        } else {
            // 取消共享
            try {
                // 看当前这个robot是不是自己的
                if(robotMapper.selectCount(wrapper) >= 1 && (sharedRobotMapper.deleteById(shareRobotDTO.getRobotId()) == 1) && (sharedRobotCache.remove(shareRobotDTO.getRobotId()))){
//                    Event event = new CancelSharedTaskEvent(shareRobotDTO.getRobotId(), uid);
//                    eventManager.publishEvent(event);
                    return true;
                }
            } catch (Exception e){
                throw new ServeException(RCode.CANCEL_SHARE_FAILED);
            }
            throw new ServeException(RCode.CANCEL_SHARE_FAILED);
        }
    }

    @Override
    public Page<SharedRobotVO> sharedRobotPage(Integer page, String taskType) {
        Page<SharedRobotDO> p = new Page<>(page, 10);
        // 判断taskType条件是否存在
        return Objects.requireNonNull(taskType).isBlank() && checkTaskType(taskType)
                ? sharedRobotMapper.selectSharedDataByTaskType(taskType, p) : sharedRobotMapper.selectSharedData(p);
    }

    @Override
    public SharedRobotVO sharedRobotInfo(Long id) {
        try {
            return sharedRobotMapper.selectSharedDataById(id);
        } catch (Exception e) {
            throw new ServeException(RCode.ROBOT_NOT_IN_SHARED);
        }
    }

    @Override
    public List<String> recommend(Integer page) {
//        return sharedRobotCache.;
        return List.of();
    }

    private static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return "," + String.join(",", list) + ",";
    }

    private Boolean checkTaskType(String taskType) {
        if (!isValidConstant(TaskTypeConstant.class, taskType)) {
            throw new ServeException("不支持的任务类型: " + taskType);
        }
        return true;
    }

    private void checkTaskTypes(List<String> taskTypes) {
        taskTypes.stream().map(this::checkTaskType);
    }

    //TODO 需要补充验证cookie是否可用
    private Boolean checkCookie(String cookie){
        return cookie!=null;
    }
}
