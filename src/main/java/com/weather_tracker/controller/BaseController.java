package com.weather_tracker.controller;

import com.weather_tracker.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public abstract class BaseController {
    @Autowired
    protected AuthenticationService authenticationService;

    public BaseController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    public void saveCookie(HttpServletResponse response, UUID session) {
        Cookie cookie = new Cookie("sessionId", session.toString());
        cookie.setMaxAge(60 * 5);
//        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
    }


    public void deleteCookie(HttpServletResponse resp, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        }
    }
}
