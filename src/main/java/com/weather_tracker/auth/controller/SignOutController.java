package com.weather_tracker.auth.controller;

import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.commons.BaseController;
import com.weather_tracker.commons.exception.InvalidParameterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SignOutController extends BaseController {

    protected SignOutController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }


    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) String sessionId,
                          HttpServletRequest req,
                          HttpServletResponse resp,
                          Model model) {
        try {
            log.trace("Attempting to find cookies");
            sessionService.signOut(sessionId);

            cookieService.delete(sessionId, req.getCookies(), resp);
            log.info("Cookies successfully delete");

            return "redirect:/home";
        } catch (InvalidParameterException e) {
            return "redirect:/home";
        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            return "redirect:/user-page";
        }
    }
}
