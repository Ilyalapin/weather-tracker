package com.weather_tracker.service;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.dao.UserDao;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class AuthenticationServiceTest {

    private static AuthenticationService authenticationService;
    private static UserDao userDao;
    private static UserRequestDto userDto;
    private static User user;
    private static String password;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService authenticationService, UserDao userDao) {
        this.authenticationService = authenticationService;
        this.userDao = userDao;
    }


    @Nested
    class CheckingOfWorkWithPersonalData {

        @BeforeEach
        void setUp() {
            userDto = new UserRequestDto();
            String login = "Login";
            password = "Password!123";
            userDto.setPassword(password);
            userDto.setLogin(login);
        }


        @org.junit.jupiter.api.Test
        void shouldThrowExceptionWhenLoginAlreadyExist() {
            String password = "Password!12345";

            UserRequestDto newUserDto = new UserRequestDto();
            newUserDto.setPassword(password);
            newUserDto.setLogin("Login");

            assertThrows(InvalidParameterException.class, () -> authenticationService.save(newUserDto));
        }


        @org.junit.jupiter.api.Test
        void shouldHashPasswordWhenSavingUser() {
            user = authenticationService.save(userDto);
            assertNotNull(user);
            assertNotEquals(password, user.getPassword());
            assertTrue(authenticationService.isChecked(password, user.getPassword()));
        }


        @AfterEach
        public void tearDown() {
            userDao.delete(user);
        }
    }


    @Nested
    class ExceptionThrowChecking {

        @org.junit.jupiter.api.Test
        void shouldThrowExceptionWhenLoginNotFound() {
            String login = "Login";
            String password = "Null!123";
            String missingLogin = "Logan";

            UserRequestDto testUserDto = new UserRequestDto();
            testUserDto.setLogin(login);
            testUserDto.setPassword(password);

            user = authenticationService.save(testUserDto);

            assertThrows(NotFoundException.class, () -> authenticationService.findByPersonalData(missingLogin, password));
        }


        @org.junit.jupiter.api.Test
        void shouldThrowExceptionWhenSessionNotFound() {
            UUID session = authenticationService.saveSession(user);
            authenticationService.deleteSession(String.valueOf(session));

            assertThrows(NotFoundException.class, () -> authenticationService.findById(session));
        }


        @org.junit.jupiter.api.Test
        void shouldThrowNotFoundExceptionWhenCookiesArrayIsEmptyOrNull() {
            assertThrows(NotFoundException.class, () -> authenticationService.findSessionId(new Cookie[]{}));
            assertThrows(NotFoundException.class, () -> authenticationService.findSessionId(null));
        }
    }


    @Nested
    class CheckingOfWorkWithSession {

        @BeforeAll
        static void setUp() {
            userDto = new UserRequestDto();
            String login = "login";
            password = "passworD!123";
            userDto.setPassword(password);
            userDto.setLogin(login);
            user = authenticationService.save(userDto);
        }


        @org.junit.jupiter.api.Test
        void shouldSaveSessionWhenSavingUser() {
            UUID session = authenticationService.saveSession(user);

            assertNotNull(authenticationService.findById(session));
        }


        @org.junit.jupiter.api.Test
        void shouldFindUserNameFromCookie() {
            UUID session = authenticationService.saveSession(user);

            Cookie sessionIdCookie = new Cookie("sessionId", session.toString());
            Cookie[] cookies = {
                    new Cookie("someCookie", "value"),
                    sessionIdCookie,
                    new Cookie("anotherCookie", "value")
            };
            String userName = authenticationService.findUserName(cookies);

            assertEquals("login", userName);
        }
    }
}
