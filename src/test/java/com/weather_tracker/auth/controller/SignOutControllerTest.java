package com.weather_tracker.auth.controller;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignOutControllerTest {

    private final WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    public SignOutControllerTest(WebApplicationContext wac) {
        this.wac = wac;
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void signInShouldOpenToSignInPage() throws Exception {
        mockMvc.perform(post("/sign-out"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
