package com.weather_tracker.controller.auth;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.auth.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class SignInControllerTest {

    private final WebApplicationContext wac;
    private UserService userService;
    private MockMvc mockMvc;


    @Autowired
    public SignInControllerTest(WebApplicationContext wac, UserService userService) {
        this.wac = wac;
        this.userService = userService;
    }


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        userService = wac.getBean(UserService.class);
    }

    @Test
    void signInShouldOpenToSignInPage() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void shouldRedirectToUserPageIfPersonalDataIsValid() throws Exception {
        String login = "User";
        String password = "Password!123";

        UserRequestDto userDto = new UserRequestDto(login, password);
        userService.save(userDto);

        mockMvc.perform(post("/sign-in")
                        .param("login", login)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-page"));
    }

    @Test
    void shouldReturnViewSignInIfUserNotFound() throws Exception {
        String login = "UseR";
        String password = "Password!123";
        String errorMessage = "User not found";

        mockMvc.perform(post("/sign-in")
                        .param("login", login)
                        .param("password", password))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andExpect(model().attribute("error", errorMessage));
    }
}
