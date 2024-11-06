package io.github.geniusay.core.counters.aop;

import io.github.geniusay.core.anno.Counters;
import io.github.geniusay.core.counters.strategy.CountersStrategy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
public class CountersAspect {

    @Resource
    private CountersStrategy countersStrategy;

    @Before("@annotation(io.github.geniusay.core.anno.Counters)")
    public void beforeActionAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Counters counters = method.getAnnotation(Counters.class);
        countersStrategy.getCounter(counters.type()).increment();
    }
}