package com.weather_tracker.auth.service;

import com.weather_tracker.auth.model.user.UserDao;
import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.auth.model.user.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class UserServiceTest {

    @Mock
    UserDao userDao;
    @Mock
    User mockUser;
    @InjectMocks
    private UserService userService;

    private User user;
    private String login;
    private String password;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        login = "Login";
        password = "Password!123";
        UserRequestDto userDto = new UserRequestDto(login, password);

        when(userDao.save(user)).thenReturn(mockUser);
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

        User testUser = userService.save(new UserRequestDto(testLogin, testPassword));
        userService.delete(testUser);

        assertThrows(NotFoundException.class, () -> userService.findByPersonalData(testUser.getLogin(), testUser.getPassword()));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExist() {
        String password = "Password!12345";

        UserRequestDto newUserDto = new UserRequestDto(login, password);

        assertThrows(InvalidParameterException.class, () -> userService.save(newUserDto));
    }
}












