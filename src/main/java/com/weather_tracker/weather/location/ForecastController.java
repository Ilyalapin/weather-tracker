package com.weather_tracker.weather.location;

import com.weather_tracker.commons.BaseController;
import com.weather_tracker.commons.exception.HttpSessionInvalidatedException;
import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/forecasts")
public class ForecastController extends BaseController {
    @Autowired
    public ForecastController(SessionService sessionService, LocationService locationService) {
        super(sessionService, locationService);
    }

    @PostMapping("/add")
    public String add(@CookieValue(value = "sessionId", required = false) String sessionId,
                      @RequestParam("locationIndex") int locationIndex,
                      @RequestParam("name") String name,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        try {
            User user = sessionService.findBySessionId(sessionId);
            List<ViewWeatherDto> forecasts = (List<ViewWeatherDto>) session.getAttribute("forecasts");

            ViewWeatherDto forecast = locationService.findByIndex(forecasts, locationIndex);

            LocationRequestDto locationRequestDto = new LocationRequestDto(
                    user,
                    name,
                    forecast.getLat(),
                    forecast.getLon()
            );
            locationService.save(locationRequestDto);

            return "redirect:/user-page";
        } catch (HttpSessionInvalidatedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user-page";
        }
    }


    @PostMapping("/delete")
    public String delete(@CookieValue(value = "sessionId", required = false) String sessionId,
                         @RequestParam("lat") double lat,
                         @RequestParam("lon") double lon,
                         RedirectAttributes redirectAttributes) {
        try {
            User user = sessionService.findBySessionId(sessionId);
            locationService.delete(user, lat, lon);

            return "redirect:/user-page";
        } catch (HttpSessionInvalidatedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user-page";
        }
    }
}
