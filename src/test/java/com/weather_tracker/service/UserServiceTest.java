package com.weather_tracker.service;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import com.weather_tracker.service.auth.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class UserServiceTest {
    private final UserService userService;
    private User user;
    private String login;
    private String password;
    private final UserRequestDto userDto;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
        this.userDto = new UserRequestDto();
    }

    @BeforeEach
    void setUp() {
        login = "Login";
        password = "Password!123";
        userDto.setPassword(password);
        userDto.setLogin(login);
        user = userService.save(userDto);
    }

    @AfterEach
    public void tearDown() {
        userService.delete(user);
    }

    @Test
    void shouldFindUserByPersonalData() {
        assertNotNull(user);
        user = userService.findByPersonalData(login, password);
        assertEquals(login, user.getLogin());
    }

    @Test
    void shouldThrowNotFoundExceptionIfUserIsNotFound() {
        String testLogin = "login";
        String testPassword = "Password!123";
        UserRequestDto testUserDto = new UserRequestDto(testLogin, testPassword);

        User testUser = userService.save(testUserDto);
        userService.delete(testUser);
        assertThrows(NotFoundException.class, () -> userService.findByPersonalData(testUser.getLogin(), testUser.getPassword()));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExist() {
        String password = "Password!12345";

        UserRequestDto newUserDto = new UserRequestDto();
        newUserDto.setPassword(password);
        newUserDto.setLogin("Login");

        assertThrows(InvalidParameterException.class, () -> userService.save(newUserDto));
    }
}












