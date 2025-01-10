package com.weather_tracker.commons.filter;

import com.weather_tracker.commons.exception.HttpSessionInvalidatedException;
import com.weather_tracker.commons.util.ValidationUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/weather_tracker/*")
public class SessionValidationFilter extends HttpFilter {
    private static final List<String> ALLOWED_URIS = Arrays.asList(
            "/sign-in",
            "/sign-up",
            "/home",
            "/css/*"
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String sessionId = req.getHeader("sessionId");
        String requestURI = req.getRequestURI();

        if (ALLOWED_URIS.contains(requestURI)) {
            chain.doFilter(req, res);
            return;
        }
        try {
            ValidationUtil.validate(sessionId);
        } catch (RuntimeException e) {
            throw new HttpSessionInvalidatedException("Please sign in again");
        }
        chain.doFilter(req, res);
    }
}
