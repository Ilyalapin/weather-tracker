package com.weather_tracker.service;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.dao.LocationDao;
import com.weather_tracker.dto.LocationRequestDto;
import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.Location;
import com.weather_tracker.entity.User;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import com.weather_tracker.service.auth.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
public class LocationServiceTest {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;
    private final UserRequestDto userDto;
    private final LocationService locationService;
    private final LocationDao locationDao;
    private LocationRequestDto locationRequestDto;

    @Autowired
    public LocationServiceTest(UserService userService,
                               LocationService locationService, SessionService sessionService,
                               CookieService cookieService, LocationDao locationDao) {
        this.userService = userService;
        this.userDto = new UserRequestDto();
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.locationDao = locationDao;
    }

    @BeforeAll
    void setUp() {
        String login = "login";
        String password = "passworD!123";
        userDto.setPassword(password);
        userDto.setLogin(login);
        User user = userService.save(userDto);
        String name = "Sant Peterburg";
        double lat = 59.9387;
        double lon = 30.3162;
        HttpServletResponse resp = mock(HttpServletResponse.class);
        UUID sessionId = sessionService.save(user);
        cookieService.save(resp, sessionId);
        locationRequestDto = new LocationRequestDto(
                user,
                name,
                lat,
                lon
        );
    }

    @Test
    void shouldSaveLocation(){
        Location location = locationService.save(locationRequestDto);

        assertNotNull(locationDao.findByCoordinates(location.getLat(), location.getLon()));
    }


    @Test
    void shouldThrowExceptionIfLocationAlreadyAdded(){
        assertThrows(InvalidParameterException.class, () -> locationService.save(locationRequestDto));
    }


    @Test
    void shouldDeleteLocation() {
        Location location = locationService.save(locationRequestDto);
        locationService.delete(locationRequestDto);

        assertThrows(NotFoundException.class, () -> locationService.findByCoordinates(location.getLat(), location.getLon()));
    }
}
