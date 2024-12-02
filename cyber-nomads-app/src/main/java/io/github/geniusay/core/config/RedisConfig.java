package io.github.geniusay.core.config;

import io.github.geniusay.pojo.DO.AnswerQuestion;
import io.github.geniusay.pojo.DO.SharedRobotDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, AnswerQuestion> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, AnswerQuestion> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置 key 的序列化方式
        template.setKeySerializer(new StringRedisSerializer());

        // 设置 value 的序列化方式
        Jackson2JsonRedisSerializer<AnswerQuestion> serializer = new Jackson2JsonRedisSerializer<>(AnswerQuestion.class);
        ObjectMapper mapper = new ObjectMapper();
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);

        // 设置 hash key 和 value 的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }


    @Bean
    public RedisTemplate<String, SharedRobotDO> sharedRobotRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, SharedRobotDO> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置 key 的序列化方式
        template.setKeySerializer(new StringRedisSerializer());

        // 设置 value 的序列化方式
        Jackson2JsonRedisSerializer<SharedRobotDO> serializer = new Jackson2JsonRedisSerializer<>(SharedRobotDO.class);
        ObjectMapper mapper = new ObjectMapper();
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);

        // 设置 hash key 和 value 的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}