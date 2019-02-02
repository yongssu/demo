package com.spring.boot.demo.async.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-27
 * Time: 上午10:27
 */
@Slf4j

public class SelfAsyncHandlerInterceptor implements AsyncHandlerInterceptor {

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("spring异步拦截器");
    }
}
