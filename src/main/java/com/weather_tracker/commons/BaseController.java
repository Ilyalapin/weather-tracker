package com.weather_tracker.commons;

import com.weather_tracker.auth.service.CookieService;
import com.weather_tracker.auth.service.SessionService;
import com.weather_tracker.auth.service.UserService;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;

public abstract class BaseController {
    protected UserService userService;
    protected SessionService sessionService;
    protected CookieService cookieService;
    protected OpenWeatherMapApiService weatherApiService;

    protected BaseController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    protected BaseController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    public BaseController(SessionService sessionService, CookieService cookieService, OpenWeatherMapApiService weatherApiService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.weatherApiService = weatherApiService;
    }
}
