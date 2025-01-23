package com.weather_tracker.auth.service;

import com.weather_tracker.commons.exception.InvalidParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashPasswordServiceTest {

    private final HashPasswordService passwordService = new HashPasswordService();

    @Test
    void shouldHashPasswordIfValid() {
        String password = "Password!123";
        String hashedPassword = passwordService.hash(password);

        assertNotEquals(password, hashedPassword);
        assertTrue(passwordService.isChecked(password, hashedPassword));
    }

    @Test
    void shouldThrowInvalidParameterExceptionIfPasswordIsNullOreEmpty() {
        String password1 = "";
        String password2 = null;

        assertThrows(InvalidParameterException.class, () -> passwordService.hash(password1));
        assertThrows(InvalidParameterException.class, () -> passwordService.hash(password2));
    }
}
