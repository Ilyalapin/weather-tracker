package com.weather_tracker.commons;

import com.weather_tracker.weather.location.LocationService;
import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.auth.service.UserService;

public abstract class BaseController {
    protected UserService userService;
    protected SessionService sessionService;
    protected CookieService cookieService;
    protected LocationService locationService;

    protected BaseController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    protected BaseController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    public BaseController(SessionService sessionService, CookieService cookieService, LocationService locationService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.locationService = locationService;
    }
}
