package com.weather_tracker.auth.controller;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.auth.service.UserService;
import com.weather_tracker.commons.BaseController;
import com.weather_tracker.commons.util.ValidationUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
@Controller
public class SignUpController extends BaseController {

    protected SignUpController(UserService userService, SessionService sessionService, CookieService cookieService) {
        super(userService, sessionService, cookieService);
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }


    @PostMapping("/sign-up")
    public String signUp(@RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpServletResponse resp,
                         Model model) {

        UserRequestDto userDto = new UserRequestDto(login, password);

        try {
            ValidationUtil.validate(userDto);

            User user = userService.save(userDto);
            log.info("User: {} saved successfully", user);

            UUID session = sessionService.save(user);
            log.info("Session: {} saved successfully", session);

            cookieService.save(resp, session);
            log.info("Cookie saved successfully");

            return "redirect:/user-page";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }
}
