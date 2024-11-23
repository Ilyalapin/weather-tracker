package com.weather_tracker.controller.auth;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SignOutController extends BaseAuthenticationController {
    protected SignOutController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }


    @PostMapping("/sign-out")
    public String signOut(@CookieValue(value = "sessionId", required = false) String sessionId,
                          HttpServletRequest req,
                          HttpServletResponse resp,
                          Model model) {
        try {
            ValidationUtil.validate(sessionId);

            log.trace("Attempting to find cookies");
            sessionService.signOut(sessionId);

            cookieService.delete(sessionId, req.getCookies(), resp);
            log.info("Cookies successfully delete");

            return "redirect:/home";
        } catch (InvalidParameterException e) {
            return "redirect:/home";
        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }
}
