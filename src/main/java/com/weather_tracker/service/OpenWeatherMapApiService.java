package com.weather_tracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.ValidationUtil;
import com.weather_tracker.dto.LocationByDirectGeocodingDto;
import com.weather_tracker.dto.WeatherRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class OpenWeatherMapApiService {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String WEATHER_API_URL = "https://api.openweathermap.org" + "/data/2.5/weather";
    private static final String GEOCODING_API_URL = "https://api.openweathermap.org" + "/geo/1.0/direct";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpHeaders headers = new HttpHeaders();

    public String createForWeatherRequest(double lat, double lon) {
        return String.valueOf(URI.create(WEATHER_API_URL
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + API_KEY
                + "&units=" + "metric"));
    }


    public String createForGeocodingRequest(String name) {
        return String.valueOf(URI.create(GEOCODING_API_URL
                + "?q=" + name
                + "&limit=6"
                + "&appid=" + API_KEY
                + "&units="
                + "metric"));
    }


    public List<LocationByDirectGeocodingDto> findByName(String name) throws JsonProcessingException {
        ValidationUtil.isValid(name);

        String formattedName = name.replace(" ", "-");

        String request = createForGeocodingRequest(formattedName);
        String response = getDataByRequest(request);

        List<LocationByDirectGeocodingDto> locations = objectMapper
                .readValue(response, new TypeReference<List<LocationByDirectGeocodingDto>>() {
                });

        if (locations.isEmpty()) {
            log.error("Location by name:{} not found", name);
            throw new NotFoundException("Locations by name: " + name + " not found");
        }
        return locations;
    }


    public List<WeatherRequestDto> findByCoordinates(List<LocationByDirectGeocodingDto> locations) throws JsonProcessingException {
        List<WeatherRequestDto> forecasts = new ArrayList<>();

        for (LocationByDirectGeocodingDto location : locations) {
            String request = createForWeatherRequest(location.getLat(), location.getLon());
            String response = getDataByRequest(request);

            WeatherRequestDto weatherRequest = objectMapper.readValue(response, WeatherRequestDto.class);
            forecasts.add(weatherRequest);
        }
        return forecasts;
    }


    protected String getDataByRequest(String request) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Objects> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(request, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }
}
