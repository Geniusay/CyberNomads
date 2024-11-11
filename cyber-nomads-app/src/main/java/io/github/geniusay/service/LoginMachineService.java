package io.github.geniusay.service;

import io.github.geniusay.pojo.DTO.VerityCodeDTO;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/11 14:54
 */
public interface LoginMachineService {

    String verity(VerityCodeDTO verityCodeDTO);

    String generateCode();

}
