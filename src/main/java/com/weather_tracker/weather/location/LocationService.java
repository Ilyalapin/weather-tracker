package com.weather_tracker.weather.location;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.MappingUtil;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiResponseDto;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiService;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class LocationService {
    private final LocationDao locationDao;
    private final OpenWeatherApiService openWeatherService;

    @Autowired
    public LocationService(LocationDao locationDao, OpenWeatherApiService openWeatherService) {
        this.locationDao = locationDao;
        this.openWeatherService = openWeatherService;
    }


    public Location save(LocationRequestDto locationDto) {
        try {
            return locationDao.save(MappingUtil.convertToEntity(locationDto));

        } catch (ConstraintViolationException e) {
            throw new InvalidParameterException("Location already exists");
        }
    }

    public void delete(User user, Double lat, Double lon) {
        try {
            locationDao.delete(user, lat, lon);
        } catch (DataBaseException e) {
            throw new DataBaseException("Could not delete location");
        }
    }


    public List<ViewWeatherDto> getByLocations(List<Location> locations) {
        try {
            List<ViewWeatherDto> savedForecasts = new ArrayList<>();
            for (Location location : locations) {
                OpenWeatherApiResponseDto weather = openWeatherService.getByLocation(location);
                ViewWeatherDto forecast = MappingUtil.convertToDto(weather);
                forecast.setName(location.getName());
                savedForecasts.add(forecast);
            }
            return savedForecasts;
        } catch (DataBaseException e) {
            throw new DataBaseException("Could not get saved forecasts");
        }
    }


    public ViewWeatherDto findByIndex(List<ViewWeatherDto> forecasts, int locationIndex) {
        try {
            return forecasts.get(locationIndex);
        } catch (NotFoundException e) {
            throw new NotFoundException("Error getting forecasts by index");
        }
    }
}
