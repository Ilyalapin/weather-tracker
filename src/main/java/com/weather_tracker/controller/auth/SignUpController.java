package com.weather_tracker.controller.auth;

import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import com.weather_tracker.service.auth.UserService;
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
public class SignUpController {
    protected UserService userService;
    protected SessionService sessionService;
    protected CookieService cookieService;

    public SignUpController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
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

            return "redirect:/weather-tracker";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-up";
        }
    }
}
