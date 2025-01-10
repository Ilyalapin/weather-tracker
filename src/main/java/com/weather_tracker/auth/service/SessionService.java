package com.weather_tracker.auth.service;

import com.weather_tracker.auth.model.session.Session;
import com.weather_tracker.auth.model.session.SessionDao;
import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.HttpSessionInvalidatedException;
import com.weather_tracker.commons.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class SessionService {
    private final SessionDao sessionDao;
    private final UserService userService;

    @Autowired
    public SessionService(SessionDao sessionDao, UserService userService) {
        this.sessionDao = sessionDao;
        this.userService = userService;
    }

    public UUID save(User user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
        Session session = new Session(uuid, user, expiresAt);
        try {
            sessionDao.save(session);
        } catch (DataBaseException e) {
            throw new DataBaseException("Error saving session");
        }
        return uuid;
    }


    public Session findById(UUID uuid) {
        return sessionDao.findById(uuid);
    }


    public void delete(String sessionId) {
        sessionDao.deleteById(UUID.fromString(sessionId));
    }


    public User findBySessionId(String sessionId) {
        try {
            Session session = findById(UUID.fromString(sessionId));
            return session.getUserId();

        } catch (NullPointerException e) {
            throw new HttpSessionInvalidatedException("Cookies have expired, please sign in again");
        }
    }


    public void signOut(String sessionId) {
        log.info("Session id:{} found successfully", sessionId);
        delete(sessionId);
    }


    public void deleteAccount(String sessionId) {
        Session session = findById(UUID.fromString(sessionId));
        User user = session.getUserId();
        try {
            userService.delete(user);
            delete(sessionId);
        } catch (NotFoundException e) {
            throw new NotFoundException("User with login " + user.getLogin() + " not found");
        }
    }
}
