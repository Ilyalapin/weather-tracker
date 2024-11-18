package com.weather_tracker.controller.auth;

import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class DeleteController {
    protected SessionService sessionService;
    protected CookieService cookieService;

    public DeleteController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping("/delete")
    public String signIn() {
        return "sign-in";
    }


    @PostMapping("/delete")
    public String deleteAccount(@CookieValue("sessionId") String sessionId, HttpServletRequest req, HttpServletResponse resp) {

        sessionService.deleteAccount(sessionId);
        log.info("Account delete successfully");
        cookieService.delete(sessionId, req.getCookies(), resp);

        return "redirect:/sign-in";
    }
}
