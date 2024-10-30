package io.github.geniusay.supertask.task;

import com.baomidou.mybatisplus.extension.api.R;

public interface TaskHandler {
    Object run();

    void log();
}
