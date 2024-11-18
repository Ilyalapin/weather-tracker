package com.weather_tracker.controller;

import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class WeatherTrackerController {
    protected SessionService sessionService;
    protected CookieService cookieService;

    public WeatherTrackerController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping("/weather-tracker")
    public String doGet(@CookieValue("sessionId") String sessionId, HttpServletRequest req, Model model) {

        if (!cookieService.isValid(sessionId, req.getCookies())) {
            return "redirect:/sign-in";
        }
        log.info("Attempting to find user name");
        String login = sessionService.findUserLogin(sessionId);
        log.info("User with login:{} found successfully", login);

        model.addAttribute("login", login);
        return "weather-tracker";
    }
}
