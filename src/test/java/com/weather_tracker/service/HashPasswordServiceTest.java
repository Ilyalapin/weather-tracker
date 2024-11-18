package com.weather_tracker.service;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.service.auth.HashPasswordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@WebAppConfiguration
public class HashPasswordServiceTest {

    @Autowired
    private HashPasswordService passwordService;

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
