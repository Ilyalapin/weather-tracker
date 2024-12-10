package com.weather_tracker.weather.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weather_tracker.auth.model.user.User;
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

    private Double lat;

    private Double lon;
}
