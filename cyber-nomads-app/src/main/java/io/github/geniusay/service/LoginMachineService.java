package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.RegisterMachineDO;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:54
 */
public interface LoginMachineService {

    RegisterMachineDO queryMachineInfo(int id);
    String generateCode();

    Boolean logout(String scriptCode);
}
