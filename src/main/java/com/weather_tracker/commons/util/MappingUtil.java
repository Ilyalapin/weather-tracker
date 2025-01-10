package com.weather_tracker.commons.util;

import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.weather.location.Location;
import com.weather_tracker.weather.location.LocationRequestDto;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiResponseDto;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import com.weather_tracker.auth.model.user.User;
import org.modelmapper.ModelMapper;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(OpenWeatherApiResponseDto.class, ViewWeatherDto.class)
                .addMapping(OpenWeatherApiResponseDto::getIndex, ViewWeatherDto::setIndex)
                .addMapping(src -> src.getMain().getTemp(), ViewWeatherDto::setTemp)
                .addMapping(src -> src.getMain().getFeels_like(), ViewWeatherDto::setFeelsLike)
                .addMapping(src -> src.getMain().getHumidity(), ViewWeatherDto::setHumidity)
                .addMapping(src -> src.getMain().getPressure(), ViewWeatherDto::setPressure)
                .addMapping(src -> src.getWind().getSpeed(), ViewWeatherDto::setSpeed)
                .addMapping(OpenWeatherApiResponseDto::getName, ViewWeatherDto::setName)
                .addMapping(src -> src.getSys().getCountry(), ViewWeatherDto::setCountry)
                .addMapping(src -> src.getCoord().getLat(), ViewWeatherDto::setLat)
                .addMapping(src -> src.getCoord().getLon(), ViewWeatherDto::setLon);
    }

    public static User convertToEntity(UserRequestDto userDto) {
        return MODEL_MAPPER.map(userDto, User.class);
    }


    public static ViewWeatherDto convertToDto(OpenWeatherApiResponseDto openWeatherApiResponseDto) {
        return MODEL_MAPPER.map(openWeatherApiResponseDto, ViewWeatherDto.class);
    }

    public static Location convertToEntity(LocationRequestDto locationRequestDto) {
        return MODEL_MAPPER.map(locationRequestDto, Location.class);
    }
}
