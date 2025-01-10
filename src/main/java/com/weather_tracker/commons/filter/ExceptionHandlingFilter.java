package com.weather_tracker.commons.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather_tracker.commons.ErrorResponseDto;
import com.weather_tracker.commons.exception.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (DataBaseException e) {
            writeErrorResponse(res, SC_INTERNAL_SERVER_ERROR, e);
        } catch (InvalidParameterException e) {
            writeErrorResponse(res, SC_BAD_REQUEST, e);
        } catch (NotFoundException e) {
            writeErrorResponse(res, SC_NOT_FOUND, e);
        } catch (SessionNotInitializedException e) {
            writeErrorResponse(res, SC_FORBIDDEN, e);
        } catch (HttpSessionInvalidatedException e) {
            writeErrorResponse(res, SC_UNAUTHORIZED, e);
        }
    }


    private void writeErrorResponse(HttpServletResponse response, int errorCode, RuntimeException e) throws
            IOException {
        response.setStatus(errorCode);
        objectMapper.writeValue(response.getWriter(), new ErrorResponseDto(errorCode, e.getMessage()));
    }
}
