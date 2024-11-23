package com.weather_tracker.controller;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.controller.auth.BaseAuthenticationController;
import com.weather_tracker.dto.LocationByDirectGeocodingDto;
import com.weather_tracker.dto.WeatherRequestDto;
import com.weather_tracker.service.OpenWeatherMapApiService;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class WeatherTrackerController extends BaseAuthenticationController {
    OpenWeatherMapApiService weatherApiService = new OpenWeatherMapApiService();

    protected WeatherTrackerController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }

    @GetMapping("/weather-tracker")
    public String doGet(@CookieValue(value = "sessionId", required = false) String sessionId,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            ValidationUtil.validate(sessionId);
            log.info("Session ID: {}", sessionId);

            log.info("Attempting to find user name");
            String login = sessionService.findUserLogin(sessionId);
            log.info("User with login:{} found successfully", login);

            model.addAttribute("login", login);
            return "weather-tracker";

        } catch (InvalidParameterException e) {
            log.error("Invalid parameter: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "home";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }


    @GetMapping("/user-search")
    public String searchLocation(@CookieValue(value = "sessionId", required = false) String sessionId,
                                 @RequestParam("name") String name,
                                 @RequestParam("login") String login,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            ValidationUtil.validate(sessionId);

            List<LocationByDirectGeocodingDto> locations = weatherApiService.findByName(name);
            List<WeatherRequestDto> forecasts = weatherApiService.findByCoordinates(locations);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("login", login);
            model.addAttribute("name", name);
            return "weather-tracker";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }
}
