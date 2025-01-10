package com.weather_tracker.weather.location;

import com.weather_tracker.commons.util.FormatingUserInputUtil;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiService;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("home")
public class HomeController {
    private final OpenWeatherApiService weatherApiService;

    @GetMapping
    public String doGet() {
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam("name") String name,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            List<ViewWeatherDto> forecasts = weatherApiService.getByName(name);

            model.addAttribute("forecasts", forecasts);
            model.addAttribute("name", FormatingUserInputUtil.format(name));

            return "home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        }
    }
}
