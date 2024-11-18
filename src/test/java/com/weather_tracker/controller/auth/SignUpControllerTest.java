package com.weather_tracker.controller.auth;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class SignUpControllerTest {
    private final WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    public SignUpControllerTest(WebApplicationContext wac) {
        this.wac = wac;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void shouldRedirectToWeatherTrackerIfPersonalDataIsValid() throws Exception {
        String login = "User";
        String password = "Password!123";

        mockMvc.perform(post("/sign-up")
                        .param("login", login)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/weather-tracker"));
    }

    @Test
    void shouldReturnViewSignUpIfPersonalDataIsInvalid() throws Exception {
        String login = "User";
        String password = "Password";
        String errorMessage = "Invalid parameter: password must contain: " +
                "at least one number, " +
                "one lowercase letter," +
                " one uppercase letter, " +
                "one special character" +
                " and contain from 6 to 20 characters";

        mockMvc.perform(post("/sign-up")
                        .param("login", login)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attribute("error", errorMessage));
    }
}
