package com.weather_tracker.commons.util;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.auth.model.user.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilTest {
    private UserRequestDto userDto;


    @BeforeEach
    public void setUp() {
        userDto = new UserRequestDto();
        userDto.setPassword("Qwerty!123");
    }


    @Test
    public void shouldGetAnInvalidParameterException_missingLogin() {
        userDto.setLogin("");
        assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));

        userDto.setLogin("Qw");
        assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
    }


    @Test
    public void shouldGetAnInvalidParameterExceptionIfInvalidPassword() {
        userDto.setPassword("");
        assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));

        userDto.setPassword("qwerty");
        assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
    }
}
