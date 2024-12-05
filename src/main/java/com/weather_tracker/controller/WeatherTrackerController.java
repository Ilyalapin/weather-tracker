package com.weather_tracker.controller;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.util.FormatingUserInputUtil;
import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.controller.auth.BaseAuthenticationController;
import com.weather_tracker.dto.LocationRequestDto;
import com.weather_tracker.dto.WeatherResponseDto;
import com.weather_tracker.entity.Session;
import com.weather_tracker.entity.User;
import com.weather_tracker.service.LocationService;
import com.weather_tracker.service.OpenWeatherMapApiService;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class WeatherTrackerController extends BaseAuthenticationController {
    OpenWeatherMapApiService weatherApiService = new OpenWeatherMapApiService();

    public WeatherTrackerController(SessionService sessionService, CookieService cookieService, LocationService locationService) {
        super(sessionService, cookieService, locationService);
    }


    @GetMapping("/weather-tracker")
    public String doGet(@CookieValue(value = "sessionId", required = false) String sessionId,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            ValidationUtil.validate(sessionId);

            Session currentSession = sessionService.findById(UUID.fromString(sessionId));

            List<WeatherResponseDto> savedForecasts = locationService.getSavedForecasts(currentSession.getUserId().getLocations());

            model.addAttribute("login", sessionService.findBySessionId(sessionId).getLogin());
            model.addAttribute("savedForecasts", savedForecasts);

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
    public String find(@CookieValue(value = "sessionId", required = false) String sessionId,
                       @RequestParam("name") String name,
                       @RequestParam("login") String login,
                       RedirectAttributes redirectAttributes,
                       Model model,
                       HttpSession session) {
        try {
            ValidationUtil.validate(sessionId);

            List<WeatherResponseDto> forecasts = weatherApiService.getByName(name);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("login", login);
            model.addAttribute("name", FormatingUserInputUtil.format(name));
            session.setAttribute("forecasts", forecasts);

            return "weather-tracker";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }


    @PostMapping("/add-location")
    public String add(@CookieValue(value = "sessionId", required = false) String sessionId,
                      @RequestParam("locationIndex") int locationIndex,
                      @RequestParam("name") String name,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        try {
            ValidationUtil.validate(sessionId);

            User user = sessionService.findBySessionId(sessionId);

            WeatherResponseDto selectedForecast = locationService.findByIndex(session, locationIndex);

            LocationRequestDto locationRequestDto = new LocationRequestDto(
                    user,
                    name,
                    selectedForecast.getLat(),
                    selectedForecast.getLon()
            );
            locationService.save(locationRequestDto);
            return "redirect:/weather-tracker";

        } catch (InvalidParameterException e){
            return "redirect:/weather-tracker";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }



    @PostMapping("/delete-location")
    public String delete(@CookieValue(value = "sessionId", required = false) String sessionId,
                         @RequestParam("lat") double lat,
                         @RequestParam("lon") double lon,
                         @RequestParam("name") String name,
                         RedirectAttributes redirectAttributes) {
        try {
            ValidationUtil.validate(sessionId);

            User user = sessionService.findBySessionId(sessionId);

            LocationRequestDto locationRequestDto = new LocationRequestDto(
                    user,
                    name,
                    lat,
                    lon
            );
            locationService.delete(locationRequestDto);
            return "redirect:/weather-tracker";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/weather-tracker";
        }
    }
}
