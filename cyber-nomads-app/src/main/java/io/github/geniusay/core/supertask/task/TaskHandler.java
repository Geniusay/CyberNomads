package io.github.geniusay.core.supertask.task;

import com.baomidou.mybatisplus.extension.api.R;

public interface TaskHandler {
    Object run();

    void log();
}
