package com.spring.boot.demo.async.controller;

import com.spring.boot.demo.async.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-23
 * Time: 上午11:39
 */
@RestController
@Controller
@Slf4j
@RequestMapping("/spring-boot/async")
public class AsyncController {

    @Resource
    private AsyncService asyncService;

    @RequestMapping("/void")
    public String asyncVoid() {
        long start = System.currentTimeMillis();
        asyncService.async();
        long end = System.currentTimeMillis();
        log.info("async void time:{}", end - start);
        return "success";
    }


    @RequestMapping("/string")
    public String asyncString() {
        long start = System.currentTimeMillis();
        String result = getResult();
        long end = System.currentTimeMillis();
        log.info("async string time:{}", end - start);
        return result;
    }

    @GetMapping("/quotes")
    public DeferredResult<String> quotes() {
        long start = System.currentTimeMillis();
        DeferredResult<String> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            deferredResult.setResult(getResult());
            return null;
        });
        long end = System.currentTimeMillis();
        log.info("async string time:{}", end - start);
        deferredResult.onTimeout(() -> {
        });
        // Save the deferredResult somewhere..
        return deferredResult;
    }

    @GetMapping("/error")
    public DeferredResult<String> error() {
        long start = System.currentTimeMillis();
        DeferredResult<String> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            deferredResult.setErrorResult(new RuntimeException("异常情况容错处理，打印异常信息").getMessage());
            return null;
        });
        long end = System.currentTimeMillis();
        log.info("async string time:{}", end - start);
        deferredResult.onTimeout(() -> {
        });
        // Save the deferredResult somewhere..
        return deferredResult;
    }

    @GetMapping("/timeout")
    public DeferredResult<String> timeout() {
        long start = System.currentTimeMillis();
        DeferredResult<String> deferredResult = new DeferredResult<>(3000L);
        CompletableFuture.supplyAsync(() -> {
            deferredResult.onTimeout(() -> {
                log.info("超时任务任务处理");
                deferredResult.setErrorResult("超时任务容错处理");
            });
            deferredResult.setResult(getResult());
            return null;
        });
        long end = System.currentTimeMillis();
        log.info("async string time:{}", end - start);
        deferredResult.onTimeout(() -> {
        });
        // Save the deferredResult somewhere..
        return deferredResult;
    }


    @GetMapping("/callable")
    public Callable<String> processUpload(final MultipartFile file) {
        // jdk8
        return () -> getResult();
//        return new Callable<String>() {
//            public String call() throws Exception {
//                return getResult();
//            }
//        };
    }


    @GetMapping("/callable/exception")
    public Callable<String> processUploadException(final MultipartFile file) {
        // jdk8
        return () -> getResult2();
//        return new Callable<String>() {
//            public String call() throws Exception {
//                return getResult();
//            }
//        };
    }

    @ExceptionHandler
    public Object customHandler(RuntimeException runtimeException) {
        log.info("相关异常处理", runtimeException);
        return "hello world";
    }

    public String getResult2() {
        throw new RuntimeException("获取结果异常");
    }

    public String getResult() {
        Future result1 = asyncService.asyncString("key1");
        Future result2 = asyncService.asyncString("key2");
        String result = "";
        try {
            result += result1.get();
            result += result2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/events")
    public ResponseBodyEmitter events() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        CompletableFuture.supplyAsync(() -> {

            try {
                emitter.send("hello world");
                emitter.send("abc");
                emitter.send(100);
                emitter.send(100000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            emitter.complete();
            return "=====hello=====";
        });
        return emitter;
    }
}
