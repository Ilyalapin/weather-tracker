package com.weather_tracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weather_tracker.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRequestDto {

    private User user;

    private String name;

    private double lat;

    private double lon;
}
