package com.weather_tracker.weather.openWeatherApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewWeatherDto {

    private int index;

    private int temp;

    private int feelsLike;

    private int humidity;

    private int pressure;

    private int speed;

    private String name;

    private String country;

    private Double lat;

    private Double lon;
}
