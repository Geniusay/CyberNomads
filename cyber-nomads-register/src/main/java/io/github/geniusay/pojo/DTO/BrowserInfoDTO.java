package io.github.geniusay.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/16 21:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrowserInfoDTO {

    private String path;
    private String version;
    private String name;

}
