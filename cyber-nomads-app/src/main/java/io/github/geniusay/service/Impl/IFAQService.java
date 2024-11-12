package io.github.geniusay.service.Impl;

import io.github.geniusay.mapper.AnswerQuestionMapper;
import io.github.geniusay.pojo.DO.AnswerQuestion;
import io.github.geniusay.service.FAQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class IFAQService implements FAQService {

    @Resource
    private AnswerQuestionMapper answerQuestionMapper;

    @Resource
    private RedisTemplate<String, AnswerQuestion> redisTemplate;

    private final Object lock = new Object();

    @PostConstruct
    public List<AnswerQuestion> init() {
        log.info("问题和回答缓存初始化...");
        redisTemplate.delete("answerQuestions");
        List<AnswerQuestion> answerQuestions = answerQuestionMapper.selectList(null);
        redisTemplate.opsForList().leftPushAll("answerQuestions", answerQuestions);
        log.info("问题和回答缓存初始化成功，当前缓存数量{}", answerQuestions.size());
        return answerQuestions;
    }


    @Override
    public List<AnswerQuestion> getFAQ() {
        List<AnswerQuestion> list = redisTemplate.opsForList().range("answerQuestions", 0, -1);
        if (list == null || list.isEmpty()){
            synchronized (lock){
                list = redisTemplate.opsForList().range("answerQuestions", 0, -1);
                if (list == null || list.isEmpty()){
                    list = init();
                }
            }
        }
        return list;
    }

    @Override
    public void addFAQ(AnswerQuestion answerQuestion) {
        redisTemplate.opsForList().leftPush("answerQuestions", answerQuestion);
        answerQuestionMapper.insert(answerQuestion);
    }
}
