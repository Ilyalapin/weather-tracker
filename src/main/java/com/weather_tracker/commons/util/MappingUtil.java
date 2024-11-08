package com.weather_tracker.commons.util;

import com.weather_tracker.dto.UserRequestDto;
import com.weather_tracker.entity.User;
import org.modelmapper.ModelMapper;

public class MappingUtil {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();


    public static User convertToEntity(UserRequestDto userDto) {
        return MODEL_MAPPER.map(userDto, User.class);
    }
}
