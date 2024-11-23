package com.weather_tracker.controller;

import com.weather_tracker.controller.auth.BaseAuthenticationController;
import com.weather_tracker.dto.LocationByDirectGeocodingDto;
import com.weather_tracker.dto.WeatherRequestDto;
import com.weather_tracker.service.OpenWeatherMapApiService;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class HomeController extends BaseAuthenticationController {
    OpenWeatherMapApiService weatherApiService = new OpenWeatherMapApiService();

    protected HomeController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }

    @GetMapping("/home")
    public String doGet() {
        return "home";
    }

    @GetMapping("/guest-search")
    public String searchLocation(@RequestParam("name") String name,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            List<LocationByDirectGeocodingDto> locations = weatherApiService.findByName(name);
            List<WeatherRequestDto> forecasts = weatherApiService.findByCoordinates(locations);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("name", name);
            return "home";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        }
    }
}
