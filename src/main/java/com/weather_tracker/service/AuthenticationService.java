package com.weather_tracker.service;

import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.MappingUtil;
import com.weather_tracker.dao.SessionDao;
import com.weather_tracker.dao.UserDao;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.Session;
import com.weather_tracker.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@Slf4j
@Component
public class AuthenticationService {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @Autowired
    public AuthenticationService(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }


    private static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            log.error("Password is null or empty");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public boolean isChecked(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            log.error("Password or hashed password is null");
            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }


    public User save(UserRequestDto userDto) {
        String hashedPassword = hashPassword(userDto.getPassword());
        userDto.setPassword(hashedPassword);

        try {
            return userDao.save(MappingUtil.convertToEntity((userDto)));
        } catch (ConstraintViolationException e) {
            throw new InvalidParameterException("User with login " + userDto.getLogin() + " already exists");
        }
    }


    public void deleteAccount(Cookie[] cookies) {
        String sessionId = findSessionId(cookies);
        Session session = findById(UUID.fromString(sessionId));
        User user = session.getUserId();
        try {
            userDao.delete(user);
            deleteSession(sessionId);
        } catch (NotFoundException e) {
            throw new NotFoundException("User with login " + user.getLogin() + " not found");
        }
    }


    public User findByPersonalData(String login, String password) {

        log.info("Attempting to find user with login: {}", login);
        User user = userDao.findByLogin(login)
                .orElseThrow(() -> {
                    log.error("User with login: {} not found", login);
                    return new NotFoundException("Incorrect login or password");
                });
        log.info("User found successfully: {}", user);

        if (!(isChecked(password, user.getPassword()))) {
            throw new NotFoundException("Incorrect password");
        }
        return user;
    }

    //*** Session ***
    public UUID saveSession(User user) {
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


    public void deleteSession(String sessionId) {
        try {
            sessionDao.deleteById(UUID.fromString(sessionId));
        } catch (NotFoundException e) {
            throw new NotFoundException("Session with uuid: " + sessionId + " not found");
        }
    }


    public String findSessionId(Cookie[] cookies) {
        log.trace("Attempting to find cookies");
        if ((cookies != null) && (cookies.length > 0)) {

            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            log.info("Session Id found successfully");
        }
        throw new NotFoundException("Cookies not found");
    }


    public void signOut(Cookie[] cookies) {
        String sessionId = findSessionId(cookies);
        log.info("Session id:{} found successfully", sessionId);

        deleteSession(sessionId);

    }


    public String findUserName(Cookie[] cookies) {
        String sessionId = findSessionId(cookies);
        Session session = findById(UUID.fromString(sessionId));
        User user = session.getUserId();
        return user.getLogin();
    }


    public boolean isCookiesValid(Cookie[] cookies, HttpServletResponse response) throws IOException {
        boolean isAuthenticated = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName()) && cookie.getValue() != null) {
                    isAuthenticated = true;
                    break;
                }
            }
        }
        if (!isAuthenticated) {
            response.sendRedirect("/sign-in");
        }
        return isAuthenticated;
    }
}
