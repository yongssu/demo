package com.spring.boot.demo.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-23
 * Time: 上午11:32
 */
@Service
@Slf4j
public class AsyncService {
    @Async
    public void async() {
        log.info("void async task start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("void async task end");
    }

    @Async
    public Future<String> asyncString(String key) {
        log.info("string async task start. key: {}", key);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("string async task end. key:{}", key);
        return new AsyncResult<>("hello, " + key);
    }
}
