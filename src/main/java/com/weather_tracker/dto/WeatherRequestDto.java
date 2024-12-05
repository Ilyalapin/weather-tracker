package com.weather_tracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherRequestDto {

    private int index;

    @JsonProperty("weather")
    private List<WeatherDto> weather;

    @JsonProperty("main")
    private MainDto main;

    @JsonProperty("wind")
    private WindDto wind;

    @JsonProperty("sys")
    private SysDto sys;

    @JsonProperty("coord")
    private CoordDto coord;
    private  String base;
    private String name;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDto{
        private String main;
        private String description;
    }

    @Data
    public static class CoordDto{
        private Double lon;
        private Double lat;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainDto{
        private Integer temp;
        private Integer feels_like;
        private Integer temp_min;
        private Integer temp_max;
        private Integer pressure;
        private Integer humidity;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WindDto{
        private Integer speed;
        private Integer deg;
        private Integer gust;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SysDto{
        private String country;
    }
}
