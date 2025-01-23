package com.weather_tracker.auth.service;

import com.weather_tracker.auth.model.session.Session;
import com.weather_tracker.auth.model.session.SessionDao;
import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.HttpSessionInvalidatedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionDao sessionDao;
    @Mock
    private UserService userService;
    private final String login = "SomeLogin";
    private final String password = "SomePassword";
    private final User user = new User(login, password);
    private final UUID uuid = UUID.fromString("4fce76e4-8256-4537-a82a-9e85b18ad52f");
    private final Session session = new Session(uuid, user);


    @Test
    void saveSuccessfully() {
        Mockito.when(sessionDao.save(Mockito.any(Session.class))).thenReturn(session);

        UUID result = sessionService.save(user);
        Assertions.assertNotNull(result);

        Mockito.verify(sessionDao, Mockito.times(1)).save(Mockito.any(Session.class));
    }

    @Test
    void shouldThrowDataBaseExceptionWhenSaveFails() {
        Mockito.when(sessionDao.save(Mockito.any(Session.class))).thenThrow(DataBaseException.class);
        Assertions.assertThrows(DataBaseException.class, () -> sessionService.save(user));
    }

    @Test
    void findByIdSuccessfully() {
        Mockito.when(sessionDao.findById(uuid)).thenReturn(session);

        Session result = sessionService.findById(uuid);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(session, result);

        Mockito.verify(sessionDao, Mockito.times(1)).findById(uuid);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenSearchFails() {
        Mockito.when(sessionDao.findById(uuid)).thenThrow(NullPointerException.class);
        Assertions.assertThrows(NullPointerException.class, () -> sessionService.findById(uuid));
    }

    @Test
    void deleteSuccessfully() {
        sessionService.delete(String.valueOf(uuid));
        Mockito.verify(sessionDao, Mockito.times(1)).deleteById(uuid);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDeleteFails() {
        Mockito.doThrow(NullPointerException.class).when(sessionDao).deleteById(Mockito.any(UUID.class));
        Assertions.assertThrows(NullPointerException.class, () -> sessionService.delete(String.valueOf(uuid)));
    }

    @Test
    void findUserBySessionIdSuccessfully() {
        SessionService spySessionService = Mockito.spy(sessionService);
        Mockito.when(spySessionService.findById(uuid)).thenReturn(session);

        User findedUser = spySessionService.findBySessionId(uuid.toString());
        Assertions.assertNotNull(findedUser);
        Assertions.assertEquals(login, findedUser.getLogin());

        Mockito.verify(spySessionService, Mockito.times(1)).findById(uuid);
    }

    @Test
    void shouldThrowHttpSessionInvalidatedExceptionWhenSearchBySessionIdFails() {
        SessionService spySessionService = Mockito.spy(sessionService);

        Mockito.when(spySessionService.findById(uuid)).thenThrow(NullPointerException.class);
        Assertions.assertThrows(HttpSessionInvalidatedException.class, () -> spySessionService.findBySessionId(uuid.toString()));
    }

    @Test
    void deleteAccountSuccessfully() {
        SessionService spySessionService = Mockito.spy(sessionService);
        Mockito.when(spySessionService.findById(uuid)).thenReturn(session);

        spySessionService.deleteAccount(String.valueOf(uuid));

        Mockito.verify(sessionDao, Mockito.times(1)).deleteById(uuid);
        Mockito.verify(userService, Mockito.times(1)).delete(user);
    }
}
