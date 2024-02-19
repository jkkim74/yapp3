package com.kt.yapp.web;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=" +
        "classpath:application-local.properties,"+
        "classpath:application.properties")
@AutoConfigureMockMvc
public class DboxControllerTest extends TestCase {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void pullDbxDataTest(){

       // mockMvc.perform(post())

    }

}