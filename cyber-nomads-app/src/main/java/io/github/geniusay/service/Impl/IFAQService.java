package io.github.geniusay.service.Impl;

import io.github.geniusay.mapper.AnswerQuestionMapper;
import io.github.geniusay.pojo.DO.AnswerQuestion;
import io.github.geniusay.service.FAQService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class IFAQService implements FAQService {

    @Resource
    private AnswerQuestionMapper answerQuestionMapper;

    @Resource
    private RedisTemplate<String, AnswerQuestion> redisTemplate;

    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        List<AnswerQuestion> answerQuestions = answerQuestionMapper.selectList(null);
        redisTemplate.opsForList().leftPushAll("answerQuestions", answerQuestions);
    }


    @Override
    public List<AnswerQuestion> getFAQ() {
        List<AnswerQuestion> list = redisTemplate.opsForList().range("answerQuestions", 0, -1);
        if (list != null && list.isEmpty()){
            synchronized (lock){
                list = redisTemplate.opsForList().range("answerQuestions", 0, -1);
                if (list != null && list.isEmpty()){
                    init();
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
