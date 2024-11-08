package com.weather_tracker.controller;

import com.weather_tracker.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


@Slf4j
@Controller
public class WeatherTrackerController extends BaseController {
    public WeatherTrackerController(AuthenticationService authenticationService) {
        super(authenticationService);
    }


    @GetMapping("/weather-tracker")
    public String doGet(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        if (authenticationService.isCookiesValid(request.getCookies(), response)) {

            log.info("Attempting to find user name");
            String login = authenticationService.findUserName(request.getCookies());
            log.info("User with login:{} found successfully", login);

            model.addAttribute("login", login);
        }
        return "weather-tracker";
    }
}
