package com.weather_tracker.service.auth;

import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.dao.SessionDao;
import com.weather_tracker.entity.Session;
import com.weather_tracker.entity.User;
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
        return sessionDao.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Session with uuid: " + uuid + " not found"));
    }


    public void delete(String sessionId) {
        try {
            sessionDao.deleteById(UUID.fromString(sessionId));
        } catch (NotFoundException e) {
            throw new NotFoundException("Session with uuid: " + sessionId + " not found");
        }
    }


    public String findUserLogin(String sessionId) {
        Session session = findById(UUID.fromString(sessionId));
        User user = session.getUserId();
        return user.getLogin();
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
