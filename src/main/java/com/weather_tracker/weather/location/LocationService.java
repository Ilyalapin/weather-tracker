package com.weather_tracker.weather.location;

import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;
import com.weather_tracker.weather.openWeatherApi.WeatherResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LocationService {
    private final LocationDao locationDao;
    private final OpenWeatherMapApiService openWeatherService;

    @Autowired
    public LocationService(LocationDao locationDao, OpenWeatherMapApiService openWeatherService) {
        this.locationDao = locationDao;
        this.openWeatherService = openWeatherService;
    }


    public Location save(LocationRequestDto locationDto) {
        Location location = new Location(
                locationDto.getUser(),
                locationDto.getName(),
                locationDto.getLat(),
                locationDto.getLon()
        );
        try {
            locationDao.save(location);
            return location;
        } catch (ConstraintViolationException e) {
            throw new InvalidParameterException("Location already exists");
        }
    }


    public void delete(LocationRequestDto locationDto) {
        try {
            locationDao.delete(locationDto.getUser(), locationDto.getLat(), locationDto.getLon());
        } catch (DataBaseException e) {
            throw new DataBaseException("Could not delete location");
        }
    }


    public List<WeatherResponseDto> getSavedForecasts(List<Location> locations) {
        try {
            List<WeatherResponseDto> savedForecasts = new ArrayList<>();
            for (Location location : locations) {
                WeatherResponseDto forecast = openWeatherService.getByLocation(location);
                forecast.setName(location.getName());
                savedForecasts.add(forecast);
            }
            return savedForecasts;
        } catch (DataBaseException e) {
            throw new DataBaseException("Could not get saved forecasts");
        }
    }


    public WeatherResponseDto findByIndex(HttpSession session, int locationIndex) {
        try {
            List<WeatherResponseDto> forecasts = (List<WeatherResponseDto>) session.getAttribute("forecasts");
            return forecasts.get(locationIndex);
        } catch (NotFoundException e) {
            throw new NotFoundException("Error getting forecasts by index");
        }
    }
}
