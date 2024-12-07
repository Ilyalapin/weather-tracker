package com.weather_tracker.auth.service;

import com.weather_tracker.commons.exception.NotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CookieService {

    public void save(HttpServletResponse response, UUID session) {
        Cookie cookie = new Cookie("sessionId", session.toString());
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
    }

    public void delete(String sessionId, Cookie[] cookies, HttpServletResponse resp) {
        try {
            for (Cookie cookie : cookies) {
                if (sessionId.equals(cookie.getValue())) {
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                }
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Cookie not found");
        }
    }
}
