package com.spring.boot.demo.async.config;

import com.spring.boot.demo.async.interceptor.SelfAsyncHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-27
 * Time: 上午10:40
 */
@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SelfAsyncHandlerInterceptor()).addPathPatterns("/**");
    }
}
