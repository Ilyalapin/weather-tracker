package com.weather_tracker.weather.location;

import com.weather_tracker.auth.model.session.Session;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.commons.BaseController;
import com.weather_tracker.commons.exception.HttpSessionInvalidatedException;
import com.weather_tracker.commons.util.FormatingUserInputUtil;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiService;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user-page")
public class UserPageController extends BaseController {
    private final OpenWeatherApiService weatherApiService;

    @Autowired
    public UserPageController(SessionService sessionService, LocationService locationService, OpenWeatherApiService weatherApiService) {
        super(sessionService, locationService);
        this.weatherApiService = weatherApiService;
    }


    @GetMapping
    public String userPade(@CookieValue(value = "sessionId", required = false) String sessionId,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            Session currentSession = sessionService.findById(UUID.fromString(sessionId));

            List<ViewWeatherDto> savedForecasts = locationService.getByLocations(currentSession.getUserId().getLocations());

            model.addAttribute("login", sessionService.findBySessionId(sessionId).getLogin());
            model.addAttribute("savedForecasts", savedForecasts);

            return "user-page";
        } catch (HttpSessionInvalidatedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user-page";
        }
    }


    @GetMapping("/search")
    public String find(@RequestParam("name") String name,
                       @RequestParam("login") String login,
                       RedirectAttributes redirectAttributes,
                       Model model,
                       HttpSession session) {
        try {
            List<ViewWeatherDto> forecasts = weatherApiService.getByName(name);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("login", login);
            model.addAttribute("name", FormatingUserInputUtil.format(name));
            session.setAttribute("forecasts", forecasts);

            return "user-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user-page";
        }
    }
}
