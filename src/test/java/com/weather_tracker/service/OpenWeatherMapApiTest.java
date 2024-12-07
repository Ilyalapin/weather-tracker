package com.weather_tracker.service;

import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.weather.location.Location;
import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.service.UserService;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;
import com.weather_tracker.weather.openWeatherApi.WeatherResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class OpenWeatherMapApiTest {

    private final UserService userService;
    private User user;
    private String name;
    private final UserRequestDto userDto;
    private final OpenWeatherMapApiService weatherApiService;

    @Autowired
    public OpenWeatherMapApiTest(UserService userService, OpenWeatherMapApiService weatherApiService) {
        this.userService = userService;
        this.userDto = new UserRequestDto();
        this.weatherApiService = weatherApiService;
    }

    @BeforeAll
    void setUp() {
        String login = "Login";
        String password = "Password!123";
        userDto.setPassword(password);
        userDto.setLogin(login);
        user = userService.save(userDto);
        name = "Moscow";
    }

    @Test
    void shouldReturnForecastsByName() throws IOException {
        List<WeatherResponseDto> forecasts = weatherApiService.getByName(name);

        assertFalse(forecasts.isEmpty());
    }


    @Test
    void shouldReturnForecastForLocation(){
        double lat = 55.7505;
        double lon = 37.6175;
        Location location = new Location(
                user,
                name,
                lat,
                lon
        );
        WeatherResponseDto weatherResponseDto = weatherApiService.getByLocation(location);

        assertEquals(weatherResponseDto.getName(), name);
    }
}
