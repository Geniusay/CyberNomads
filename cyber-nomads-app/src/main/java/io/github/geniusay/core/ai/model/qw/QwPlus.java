package io.github.geniusay.core.ai.model.qw;

import io.github.geniusay.core.ai.config.QwConfig;
import org.springframework.stereotype.Component;

import static io.github.geniusay.constants.AIConstant.QW_PLUS;

/**
 * 描述: 千问模型 (qwen-plus)
 * @author suifeng
 * 日期: 2025/3/3
 */
@Component
public class QwPlus extends AbstractQwModel {

    public QwPlus(QwConfig config) {
        super(config);
    }

    @Override
    public String getName() {
        return QW_PLUS;
    }

    @Override
    public String description() {
        return "通义千问 (plus版本) ";
    }


    @Override
    protected String getVersion() {
        return "qwen-plus";
    }
}
