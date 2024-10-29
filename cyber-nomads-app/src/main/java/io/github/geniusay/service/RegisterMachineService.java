package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.RegisterMachineDO;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/29 19:39
 */
public interface RegisterMachineService {

    RegisterMachineDO queryMachineInfo(int id);

}
