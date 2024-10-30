package io.github.geniusay.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/30 15:05
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UCookie {
    private String name;
    private String value;

}
