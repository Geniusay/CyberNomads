package io.github.geniusay.service.Impl;

import io.github.geniusay.mapper.RegisterMachineMapper;
import io.github.geniusay.pojo.DO.RegisterMachineDO;
import io.github.geniusay.service.RegisterMachineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 19:40
 */
@Service
public class IRegisterMachineService implements RegisterMachineService {

    @Resource
    RegisterMachineMapper mapper;
    @Override
    public RegisterMachineDO queryMachineInfo(int id) {
        return mapper.selectById(id);
    }
}
