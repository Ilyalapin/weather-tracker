package com.weather_tracker.commons.util;

import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilTest {
    private UserRequestDto userDto;

    @Nested
    class CheckingValidationLogin {

        @BeforeEach
        public void setUp() {
            userDto = new UserRequestDto();
            userDto.setPassword("Qwerty!123");
        }


        @Test
        public void shouldGetAnInvalidParameterException_missingLogin() {
            userDto.setLogin("");
            assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
        }


        @Test
        public void shouldGetAnInvalidParameterException_invalidLogin() {
            userDto.setLogin("Qw");
            assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
        }
    }


    @Nested
    class CheckingValidationPassword {

        @BeforeEach
        public void setUpUser() {
            userDto = new UserRequestDto();
            userDto.setLogin("Qwerty");
        }

        @Test
        public void shouldGetAnInvalidParameterException_missingPassword() {

            userDto.setPassword("");
            assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
        }


        @Test
        public void shouldGetAnInvalidParameterException_invalidPassword() {
            userDto.setPassword("qwerty");
            assertThrows(InvalidParameterException.class, () -> ValidationUtil.validate(userDto));
        }
    }
}
