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
public class SignInController extends BaseAuthenticationController {
    protected SignInController(UserService userService, SessionService sessionService, CookieService cookieService) {
        super(userService, sessionService, cookieService);
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }


    @PostMapping("/sign-in")
    public String signIn(@RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpServletResponse resp,
                         Model model) {

        UserRequestDto userDto = new UserRequestDto(login, password);
        try {
            ValidationUtil.validate(userDto);

            User user = userService.findByPersonalData(login, password);
            log.info("User: {} saved successfully", user);

            UUID session = sessionService.save(user);
            log.info("Session: {} saved successfully", session);

            cookieService.save(resp, session);
            log.info("Cookie saved successfully");

            return "redirect:/weather-tracker";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }
    }
}
