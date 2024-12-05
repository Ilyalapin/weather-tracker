package com.weather_tracker.controller.auth;

import com.weather_tracker.service.LocationService;
import com.weather_tracker.service.auth.CookieService;
import com.weather_tracker.service.auth.SessionService;
import com.weather_tracker.service.auth.UserService;

public abstract class BaseAuthenticationController {
    protected UserService userService;
    protected SessionService sessionService;
    protected CookieService cookieService;
    protected LocationService locationService;

    protected BaseAuthenticationController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    protected BaseAuthenticationController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    public BaseAuthenticationController(SessionService sessionService, CookieService cookieService, LocationService locationService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.locationService = locationService;
    }
}