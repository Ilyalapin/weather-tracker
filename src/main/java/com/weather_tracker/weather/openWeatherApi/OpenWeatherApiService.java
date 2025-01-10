package com.weather_tracker.weather.openWeatherApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather_tracker.commons.exception.NotFoundException;
import com.weather_tracker.commons.util.FormatingUserInputUtil;
import com.weather_tracker.commons.util.MappingUtil;
import com.weather_tracker.weather.location.Location;
import com.weather_tracker.weather.location.LocationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class OpenWeatherApiService {
    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${geocoding.api.url}")
    private String geocodingApiUrl;
    private static final String API_KEY = System.getenv("API_KEY");
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OpenWeatherApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private String createForWeatherRequest(double lat, double lon) {
        return String.valueOf(URI.create(weatherApiUrl
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + API_KEY
                + "&units=" + "metric"));
    }


    private String createForGeocodingRequest(String name) {
        return String.valueOf(URI.create(geocodingApiUrl
                + "?q=" + name
                + "&limit=6"
                + "&appid=" + API_KEY
                + "&units="
                + "metric"));
    }


    public List<LocationRequestDto> findByName(String name) throws JsonProcessingException {
        String formattedName = FormatingUserInputUtil.format(name);
        String request = createForGeocodingRequest(formattedName);
        String response = getDataByRequest(request);

        List<LocationRequestDto> locations = objectMapper.readValue(response, new TypeReference<>() {});

        if (locations.isEmpty()) {
            log.error("Location by name:{} not found", name);
            throw new NotFoundException("Locations by name: " + name + " not found");
        }
        return locations;
    }


    public List<ViewWeatherDto> getByLocations(List<LocationRequestDto> locations) throws JsonProcessingException {
        List<ViewWeatherDto> forecasts = new ArrayList<>();

        for (LocationRequestDto location : locations) {
            String request = createForWeatherRequest(location.getLat(), location.getLon());
            String response = getDataByRequest(request);

            OpenWeatherApiResponseDto requestDto = objectMapper.readValue(response, OpenWeatherApiResponseDto.class);

            ViewWeatherDto responseDto = MappingUtil.convertToDto(requestDto);
            forecasts.add(responseDto);
        }
        return forecasts;
    }


    public List<ViewWeatherDto> getByName(String name) throws JsonProcessingException {
        List<LocationRequestDto> locations = findByName(name);
        List<ViewWeatherDto> forecasts = getByLocations(locations);

        for (int i = 0; i < forecasts.size(); i++) {
            forecasts.get(i).setIndex(i);
        }
        return forecasts;
    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


    private String getDataByRequest(String request) {
        HttpHeaders headers = createHeaders();
        HttpEntity<Objects> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(request, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }


    public OpenWeatherApiResponseDto getByLocation(Location location) {
        try {
            String request = createForWeatherRequest(location.getLat(), location.getLon());
            String response = getDataByRequest(request);

            return objectMapper.readValue(response, OpenWeatherApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while parsing JSON", e);
        }
    }
}
