package com.weather_tracker.weather.location;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.auth.service.UserService;
import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;
import com.weather_tracker.weather.openWeatherApi.WeatherResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
public class LocationServiceTest {
    @Mock
    private LocationDao locationDao;

    @Mock
    private OpenWeatherMapApiService openWeatherMapApiService;

    @Mock
    UserService userService;

    @InjectMocks
    private LocationService locationService;
    private LocationRequestDto locationRequestDto1;
    private Location location1;
    private Location location2 = new Location();
    private final User user = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        locationService = new LocationService(locationDao, openWeatherMapApiService);
        when(userService.save(any(UserRequestDto.class))).thenReturn(user);
        String name1 = "Sant Peterburg";
        String name2 = "Ryazan";
        double lat1 = 59.9387;
        double lon1 = 30.3162;
        double lat2 = 54.6296;
        double lon2 = 39.7425;
        locationRequestDto1 = new LocationRequestDto(
                user,
                name1,
                lat1,
                lon1
        );
        LocationRequestDto locationRequestDto2 = new LocationRequestDto(
                user,
                name2,
                lat2,
                lon2
        );
        location1 = locationService.save(locationRequestDto1);
        location2 = locationService.save(locationRequestDto2);
    }


    @Test
    void shouldSaveLocation() {
        assertEquals("Sant Peterburg", location1.getName());
        assertEquals(59.9387, location1.getLat(), 0);
        assertEquals(30.3162, location1.getLon(), 0);
    }


    @Test
    void shouldDeleteLocation() {
        locationService.delete(locationRequestDto1);
        doThrow(NotFoundException.class).when(locationDao).delete(locationRequestDto1.getUser(),locationRequestDto1.getLat(),locationRequestDto1.getLon());
    }


    @Test
    void shouldGetAllSavedLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        WeatherResponseDto forecast1 = new WeatherResponseDto();
        WeatherResponseDto forecast2 = new WeatherResponseDto();

        when(openWeatherMapApiService.getByLocation(location1)).thenReturn(forecast1);
        when(openWeatherMapApiService.getByLocation(location2)).thenReturn(forecast2);

        List<WeatherResponseDto> savedLocations = locationService.getSavedForecasts(locations);

        assertEquals(2, savedLocations.size());
        assertEquals("Sant Peterburg", savedLocations.get(0).getName());
        assertEquals("Ryazan", savedLocations.get(1).getName());

        verify(openWeatherMapApiService, times(1)).getByLocation(location1);
        verify(openWeatherMapApiService, times(1)).getByLocation(location2);
    }
}
