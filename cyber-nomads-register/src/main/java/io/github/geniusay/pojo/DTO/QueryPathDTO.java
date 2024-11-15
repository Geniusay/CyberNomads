package io.github.geniusay.pojo.DTO;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/15 21:58
 */
@Data
@Builder
public class QueryPathDTO {

    DriverPathDTO pathDTO;
    String errorMsg;

}
