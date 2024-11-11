package io.github.geniusay.schedule.strategy;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author welsir
 * @Date 2024/11/10 20:11
 */
@Component
@Data
public abstract class AbstractWorkerExecute implements WorkerExecuteStrategy{

    Map<Long,Integer> robotWorkingCount = new ConcurrentHashMap<>();
    @Resource
    ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, AbstractWorkerExecute> beans = applicationContext.getBeansOfType(AbstractWorkerExecute.class);

    }

    @Override
    public void execute(Long workerId) {
        robotWorkingCount.compute(workerId, (id, count) -> {
            if (count == null) {
                return 1;
            } else {
                return count + 1;
            }
        });
        doHandle(workerId);
    }

    public abstract void doHandle(Long taskId);
}
