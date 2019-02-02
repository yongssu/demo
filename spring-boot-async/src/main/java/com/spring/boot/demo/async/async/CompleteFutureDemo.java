package com.spring.boot.demo.async.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-30
 * Time: 下午3:00
 */
public class CompleteFutureDemo {

    public static void apply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(s -> s + " world");
        System.out.println(future.get());
    }

    public static void applyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello")
                .thenApplyAsync(s -> s + " world");
        System.out.println(future.get());
    }

    public static void compose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " world"));
        System.out.println(future.get());
    }

    public static void combine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello")
                .thenCombine(CompletableFuture.supplyAsync(() -> " world"), (s1, s2) -> s1 + s2);
        System.out.println(future.get());
    }

    public static void error1() throws ExecutionException, InterruptedException {
        String name = null;
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("任务处理异常模拟");
            }
            return name;
        }).handle((s, t) -> s!= null ? s : t.getMessage());
        System.out.println(future.get());
    }

    public static void error2() throws ExecutionException, InterruptedException {
        String name = null;
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("任务处理异常模拟");
            }
            return name;
        });
//        future.completeExceptionally(new RuntimeException("异步任务执行异常统一封装"));
//        System.out.println(future.get());
    }

    /**
     * CompleteFuture模拟并发任务，并组合结果，注意其中任务异常的处理
     */
    public static void demo1() {
        List<String> result = asyncTask();
        System.out.println(result);
    }

    public static List<String> asyncTask() {
        List<String> params = new ArrayList<>();
        params.add("task1");
        params.add("task2");
        params.add("task3");
        List<CompletableFuture<String>> futures = params.stream().map(t -> CompletableFuture.supplyAsync(() ->task(t)))
                .collect(Collectors.toList());
        // 任务触发与结果获取分开，使得获取结果不阻塞任务的执行
        return futures.stream().map(t -> t.handle((s, e) -> e != null ? "default value" : s))
                .map(t -> {
                    try {
                        try {
                            return t.get(10, TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return "default value2";
                })
                .collect(Collectors.toList());
    }

    public static String task(String no) {
        return "hello " + no;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        apply();
        applyAsync();
        compose();
        combine();
        error1();
        error2();
        demo1();
    }
}
