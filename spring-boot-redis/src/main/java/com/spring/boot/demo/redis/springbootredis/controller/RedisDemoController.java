package com.spring.boot.demo.redis.springbootredis.controller;

import com.spring.boot.demo.redis.springbootredis.redis.RedisOpsDemo;
import com.spring.boot.demo.redis.springbootredis.redis.RedisStringDemo;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yongssu
 * Date: 19-1-19
 * Time: 下午9:56
 */
@RestController
public class RedisDemoController {

    @Resource
    private RedisOpsDemo redisOpsDemo;
    @Resource
    private RedisStringDemo redisStringDemo;

    @RequestMapping("/list")
    public String testList() {
        return redisOpsDemo.list();
    }

    @RequestMapping("/set")
    public String testSet() {
        return redisOpsDemo.set();
    }

    @RequestMapping("/hash1")
    public Map<String, Object> testHash1() {
        return redisOpsDemo.hash1();
    }

    @RequestMapping("/hash2")
    public Map<String, Object> testHash2() {
        return redisOpsDemo.hash2();
    }

    @RequestMapping("/hash3")
    public Map<String, Object> testHash3() {
        return redisOpsDemo.hash3();
    }

    @RequestMapping("/list2")
    public String testList2() {
        return redisStringDemo.string();
    }

    @RequestMapping("/pipeline")
    public List<String> pipeline() {
        return redisStringDemo.pipeline();
    }

    @RequestMapping("/lua")
    public boolean lua() {
        return redisOpsDemo.lua();
    }
}
