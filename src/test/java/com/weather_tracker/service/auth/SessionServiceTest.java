package com.weather_tracker.service.auth;

import com.weather_tracker.auth.model.session.Session;
import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.auth.service.UserService;
import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.NotFoundException;
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
public class SessionServiceTest {
    private UserRequestDto userDto;
    private final SessionService sessionService;
    private final UserService userService;
    private User user;
    private UUID session;

    @Autowired
    public SessionServiceTest(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        userDto = new UserRequestDto();
        String login = "login";
        String password = "passworD!123";
        userDto.setPassword(password);
        userDto.setLogin(login);
    }

    @Test
    void shouldSaveSessionWhenSavingUser() {
        session = sessionService.save(user);
        assertNotNull(sessionService.findById(session));
    }

    @Test
    void shouldFindSessionById() {
        session = sessionService.save(user);
        Session findedSession = sessionService.findById(session);
        assertEquals(session, findedSession.getId());
    }

    @Test
    void shouldFindUserLoginBySessionId() {
        user = userService.save(userDto);
        session = sessionService.save(user);
        String userLogin = sessionService.findBySessionId(String.valueOf(session)).getLogin();

        assertEquals("login", userLogin);
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        session = sessionService.save(user);
        sessionService.delete(String.valueOf(session));

        assertThrows(NotFoundException.class, () -> sessionService.findById(session));
    }
}

