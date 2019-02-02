package com.spring.boot.demo.redis.springbootredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.demo.redis.springbootredis.redis.RedisOpsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * User: yongssu
 * Date: 19-1-19
 * Time: 下午10:03
 */
@Configuration
public class AppConfig {

    /**
     * 单例
     * @return
     */
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
//    }

    /**
     * 集群
     */
    @Autowired
    RedisClusterProperties redisClusterProperties;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisClusterProperties.getNodes());
        redisClusterConfiguration.setPassword(RedisPassword.of("test"));

        RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration);
        return redisConnectionFactory;
    }

    @Bean
    public Jackson2JsonRedisSerializer Jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        // 这个只能指定具体的类对象，不能自由指定
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(RedisOpsDemo.User.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        return jacksonSeial;

    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // 值采用json序列化
        template.setValueSerializer(new StringRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public ListOperations<String, String> listOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, String> setOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public HashOperations<String, String, String> hashOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public RedisScript<Boolean> script() {
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("scripts/checkandset.lua"));
        try {
            return RedisScript.of(scriptSource.getScriptAsString(), Boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("script exception");
    }
}
