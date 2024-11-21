package io.github.geniusay.schedule.storage;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/10 20:11
 */
@Component
@Data
public abstract class AbstractWorkerStorage implements WorkerStorage {

    @Resource
    ApplicationContext applicationContext;

    @PostConstruct
    public void start(){
        Map<String, AbstractWorkerStorage> beans = applicationContext.getBeansOfType(AbstractWorkerStorage.class);
        beans.forEach((name,v)->{
            v.init();
        });
    }

    @Override
    public void removeRobotWorker(Long workerId) {

    }
    public abstract void init();
}
