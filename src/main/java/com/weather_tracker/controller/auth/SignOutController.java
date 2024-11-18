package com.weather_tracker.controller.auth;

import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SignOutController {
    protected SessionService sessionService;
    protected CookieService cookieService;

    public SignOutController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping("/sign-out")
    public String signIn() {
        return "redirect:/sign-in";
    }

    @PostMapping("/sign-out")
    public String signOut(@CookieValue("sessionId") String sessionId,
                          HttpServletRequest req,
                          HttpServletResponse resp,
                          Model model) {
        try {
            log.trace("Attempting to find cookies");
            sessionService.signOut(sessionId);

            cookieService.delete(sessionId, req.getCookies(), resp);
            log.info("Cookies successfully delete");

            return "redirect:/sign-in";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());

            return "redirect:/weather-tracker";
        }
    }
}
