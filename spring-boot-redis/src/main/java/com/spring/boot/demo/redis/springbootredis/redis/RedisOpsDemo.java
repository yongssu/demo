package com.spring.boot.demo.redis.springbootredis.redis;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * User: yongssu
 * Date: 19-1-20
 * Time: 上午11:07
 */
@Component
public class RedisOpsDemo {

    @Resource
    private HashOperations<String, String, Object> hashOperations;

    @Resource
    private ListOperations<String, String> listOperations;

    @Resource
    private SetOperations<String, String> setOperations;


    public String list() {
        listOperations.leftPush("list_key", "value1");
        listOperations.leftPush("list_key", "value2");
        String value = listOperations.index("list_key", 1);
        return value;
    }

    public String set() {
        setOperations.add("set_key", "value1", "value2");
        String value = setOperations.pop("set_key");
        return value;
    }

    public Map<String, Object> hash1() {
        hashOperations.put("hash_key1", "hash_key_1", "hash_value_1");
        Map<String, Object> map = hashOperations.entries("hash_key");
        return map;
    }

    public Map<String, Object> hash2() {
        Map<String, Object> map = new HashMap<>();
        map.put("hash_key2", "hash_value_1");
        map.put("hash_key2", "hash_value_2");
        hashOperations.putAll("hash_key2", map);
        return hashOperations.entries("hash_key2");
    }

    public Map<String, Object> hash3() {
        Map<String, Object> map = new HashMap<>();
        map.put("hash_key3", new User("zhangsan", 10));
        map.put("hash_key4", new User("lisi", 11));
        hashOperations.putAll("hash_master_key3", map);
        return hashOperations.entries("hash_master_key3");
    }

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisScript<Boolean> script;
    public Boolean lua() {
        return (Boolean)redisTemplate.execute(script, Lists.newArrayList("lua_key"), Lists.newArrayList("lua_value1", "lua_value2"));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class User {
        private String name;
        private Integer age;
    }
}
