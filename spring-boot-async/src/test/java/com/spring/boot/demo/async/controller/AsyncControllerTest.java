package com.spring.boot.demo.async.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Description:
 * User: yongssu
 * Date: 18-12-23
 * Time: 上午11:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
    }

    @Test
    public void asyncVoid() throws Exception {
        this.mockMvc.perform(get("/spring-boot/async/void").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void asyncString() throws Exception {
        this.mockMvc.perform(get("/spring-boot/async/string").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}