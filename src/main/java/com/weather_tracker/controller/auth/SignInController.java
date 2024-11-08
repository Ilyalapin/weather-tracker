package com.weather_tracker.controller.auth;

import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.controller.BaseController;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import com.weather_tracker.service.AuthenticationService;
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
public class SignInController extends BaseController {

    public SignInController(AuthenticationService authenticationService) {
        super(authenticationService);
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }


    @PostMapping("/sign-in")
    public String signIn(@RequestParam("login") String login,
                         @RequestParam("password") String password,
                         HttpServletResponse response,
                         Model model) {

        UserRequestDto userDto = new UserRequestDto(login, password);
        try {
            ValidationUtil.validate(userDto);

            User user = authenticationService.findByPersonalData(login, password);
            log.info("User: {} saved successfully", user);

            UUID session = authenticationService.saveSession(user);
            log.info("Session: {} saved successfully", session);

            saveCookie(response, session);
            log.info("Cookie saved successfully");

            return "redirect:/weather-tracker";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }
    }
}
