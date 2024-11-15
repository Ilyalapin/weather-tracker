package com.weather_tracker.controller.auth;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.service.AuthenticationService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class SignInControllerTest {

    private final WebApplicationContext wac;
    private AuthenticationService authenticationService;
    private MockMvc mockMvc;


    @Autowired
    public SignInControllerTest(WebApplicationContext wac, AuthenticationService authenticationService) {
        this.wac = wac;
        this.authenticationService = authenticationService;
    }


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        authenticationService = wac.getBean(AuthenticationService.class);
    }


    @Test
    void signInShouldRedirectToSignInPage() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
