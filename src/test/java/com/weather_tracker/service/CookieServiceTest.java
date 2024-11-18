package com.weather_tracker.service;

import com.weather_tracker.commons.config.TestConfig;
import com.weather_tracker.commons.config.WebAppInitializer;
import com.weather_tracker.service.auth.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CookieServiceTest {
    private CookieService cookieService;
    private HttpServletResponse response;
    private UUID sessionId;

    @BeforeEach
    public void setUp() {
        cookieService = new CookieService();
        response = mock(HttpServletResponse.class);
        sessionId = UUID.randomUUID();
        cookieService.save(response, sessionId);
    }

    @Test
    void shouldSaveCookie() {
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie addedCookie = cookieCaptor.getValue();

        assertEquals("sessionId", addedCookie.getName());
        assertEquals(sessionId.toString(), addedCookie.getValue());
        assertEquals(60 * 5, addedCookie.getMaxAge());
    }

    @Test
    void shouldDeleteCookie() {
        Cookie sessionIdCookie = new Cookie("sessionId", sessionId.toString());
        Cookie[] cookies = {
                new Cookie("someCookie", "value"),
                sessionIdCookie,
                new Cookie("anotherCookie", "value")
        };
        cookieService.delete(String.valueOf(sessionId), cookies, response);

        assertEquals(sessionId.toString(), sessionIdCookie.getValue());
        assertEquals(0, sessionIdCookie.getMaxAge());
    }

    @Test
    void shouldReturnTrueIfCookieExists() {
        Cookie sessionIdCookie = new Cookie("sessionId", sessionId.toString());
        Cookie[] cookies = {
                new Cookie("someCookie", "value"),
                sessionIdCookie,
                new Cookie("anotherCookie", "value")
        };
        assertTrue(cookieService.isValid(sessionId.toString(), cookies));
    }
}
