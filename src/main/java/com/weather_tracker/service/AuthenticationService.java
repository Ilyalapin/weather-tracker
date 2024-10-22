//package com.weather_tracker.service;
//
//import com.example.weather.commons.exception.NotFoundException;
//import com.example.weather.dao.SessionDao;
//import com.example.weather.dao.UserDao;
//import com.example.weather.dto.UserRequestDto;
//import com.example.weather.entity.Session;
//import com.example.weather.entity.User;
//import jakarta.servlet.http.Cookie;
//import org.mindrot.jbcrypt.BCrypt;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static com.example.weather.commons.util.MappingUtil.convertToEntity;
//
//
//public class AuthenticationService {
//
//private final UserDao userDao = new UserDao();
//private final SessionDao sessionDao = new SessionDao();
//
//
//    private static String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
//
//
//    public User add(UserRequestDto userDto) {
//        String hashedPassword = hashPassword(userDto.getPassword());
//        userDto.setPassword(hashedPassword);
//
//        return userDao.add(convertToEntity(userDto));
//    }
//
//
//    public User getByLogin(String login) {
//        User user = userDao.getByLogin(login)
//                .orElseThrow(() -> new NotFoundException("User with login: " + login + " not found"));
//        return user;
//    }
//
//
//    public UUID add(User user) {
//        UUID uuid = UUID.randomUUID();
//        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
//        Session session = new Session(uuid,user, expiresAt);
//        sessionDao.add(session);
//        return uuid;
//    }
//
//
//public Session getById(UUID uuid) {
//        Session session = sessionDao.getById(uuid)
//                .orElseThrow(() -> new NotFoundException("Session with uuid: " + uuid + " not found"));
//        return session;
//}
//
//    public Cookie get(Cookie[] cookies) {
//        if (cookies == null || cookies.length < 1) {
//            throw new NotFoundException("Cookie list is empty");
//        }
//            for (Cookie cookie : cookies) {
//                if ("sessionId".equals(cookie.getName())) {
//                    return cookie;
//                }
//            }
//            throw new NotFoundException("Cookie not found");
//
//
//    }
//
//
//
//
//
//}
