package com.weather_tracker.auth.service;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.model.user.UserDao;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.commons.exception.InvalidParameterException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private HashPasswordService hashPasswordService;
    @InjectMocks
    private UserService userService;
    private final String login = "SomeLogin";
    private final String password = "SomePassword";
    private final UserRequestDto userDto = new UserRequestDto();
    private final User user = new User(login, password);


    @Test
    void saveSuccessfully(){
        Mockito.when(hashPasswordService.hash(Mockito.any())).thenReturn(password);
        Mockito.when(userDao.save(Mockito.any(User.class))).thenReturn(user);

        User testUser = userService.save(userDto);

        assertNotNull(testUser);
        assertEquals(testUser.getLogin(), login);

        Mockito.verify(userDao, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenSaveFails(){
        Mockito.when(userDao.save(Mockito.any(User.class))).thenThrow(ConstraintViolationException.class);
        assertThrows(InvalidParameterException.class, () -> userService.save(userDto));
    }

    @Test
    void findByPersonalDataSuccessfully(){
        Mockito.when(userDao.findByLogin(login)).thenReturn(user);
        Mockito.when(hashPasswordService.isChecked(password,user.getPassword())).thenReturn(true);

        User testUser = userService.findByPersonalData(login,password);

        assertNotNull(testUser);
        assertEquals(testUser.getLogin(), login);
        Mockito.verify(userDao, Mockito.times(1)).findByLogin(login);
    }

    @Test
    void deleteSuccessfully(){
        userService.delete(user);
        Mockito.verify(userDao, Mockito.times(1)).delete(Mockito.any(User.class));
    }
}
