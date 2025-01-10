package com.weather_tracker.auth.controller;

import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.commons.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/delete")
public class DeleteController extends BaseController {
    protected DeleteController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }


    @PostMapping
    public String delete(@CookieValue(value = "sessionId", required = false) String sessionId,
                         HttpServletRequest req,
                         HttpServletResponse resp,
                         Model model) {
        try {
            sessionService.deleteAccount(sessionId);
            log.info("Account delete successfully");
            cookieService.delete(sessionId, req.getCookies(), resp);

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "home";
        }
    }
}
