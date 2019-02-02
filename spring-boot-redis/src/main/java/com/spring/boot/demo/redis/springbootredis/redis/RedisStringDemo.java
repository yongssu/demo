package com.spring.boot.demo.redis.springbootredis.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: yongssu
 * Date: 19-1-20
 * Time: 下午12:13
 */
@Component
public class RedisStringDemo {

    @Resource
    public StringRedisTemplate stringRedisTemplate;

    public String string() {
        stringRedisTemplate.boundListOps("list_key2").leftPush("list_key2_value");
        return stringRedisTemplate.boundListOps("list_key2").index(0);
    }

    public List<String> pipeline() {
        //pop a specified number of items from a queue
        List<Object> results = stringRedisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                        for (int i = 0; i < 100; i++) {
                            stringRedisConn.rPop("myqueue");
                        }
                        return null;
                    }
                });
        List<String> result = new ArrayList<>();
        results.forEach(item -> result.add((String) item));
        return result;
    }
}
