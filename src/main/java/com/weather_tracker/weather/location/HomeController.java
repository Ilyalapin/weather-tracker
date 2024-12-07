package com.weather_tracker.weather.location;

import com.weather_tracker.commons.BaseController;
import com.weather_tracker.commons.util.FormatingUserInputUtil;
import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;
import com.weather_tracker.weather.openWeatherApi.WeatherResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class HomeController extends BaseController {
    OpenWeatherMapApiService weatherApiService = new OpenWeatherMapApiService();

    protected HomeController(SessionService sessionService, CookieService cookieService) {
        super(sessionService, cookieService);
    }

    @GetMapping("/home")
    public String doGet() {
        return "home";
    }

    @GetMapping("/guest-search")
    public String search(@RequestParam("name") String name,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            List<WeatherResponseDto> forecasts = weatherApiService.getByName(name);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("name", FormatingUserInputUtil.format(name));
            return "home";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        }
    }
}
