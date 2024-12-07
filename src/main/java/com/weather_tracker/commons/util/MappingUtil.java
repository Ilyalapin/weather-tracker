package com.weather_tracker.commons.util;

import com.weather_tracker.auth.model.user.UserRequestDto;
import com.weather_tracker.weather.openWeatherApi.WeatherRequestDto;
import com.weather_tracker.weather.openWeatherApi.WeatherResponseDto;
import com.weather_tracker.auth.model.user.User;
import org.modelmapper.ModelMapper;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(WeatherRequestDto.class, WeatherResponseDto.class)
                .addMapping(WeatherRequestDto::getIndex, WeatherResponseDto::setIndex)
                .addMapping(src -> src.getMain().getTemp(), WeatherResponseDto::setTemp)
                .addMapping(src -> src.getMain().getFeels_like(), WeatherResponseDto::setFeelsLike)
                .addMapping(src -> src.getMain().getHumidity(), WeatherResponseDto::setHumidity)
                .addMapping(src -> src.getMain().getPressure(), WeatherResponseDto::setPressure)
                .addMapping(src -> src.getWind().getSpeed(), WeatherResponseDto::setSpeed)
                .addMapping(WeatherRequestDto::getName, WeatherResponseDto::setName)
                .addMapping(src -> src.getSys().getCountry(), WeatherResponseDto::setCountry)
                .addMapping(src -> src.getCoord().getLat(), WeatherResponseDto::setLat)
                .addMapping(src -> src.getCoord().getLon(), WeatherResponseDto::setLon);
    }

    public static User convertToEntity(UserRequestDto userDto) {
        return MODEL_MAPPER.map(userDto, User.class);
    }


    public static WeatherResponseDto convertToDto(WeatherRequestDto weatherRequestDto) {
        return MODEL_MAPPER.map(weatherRequestDto, WeatherResponseDto.class);
    }
}
