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
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class OpenWeatherMapApiService {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String WEATHER_API_URL = "https://api.openweathermap.org" + "/data/2.5/weather";
    private static final String GEOCODING_API_URL = "https://api.openweathermap.org" + "/geo/1.0/direct";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final HttpHeaders headers;

    @Autowired
    public OpenWeatherMapApiService(RestTemplate restTemplate, ObjectMapper objectMapper, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.headers = headers;
    }

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


    public List<LocationRequestDto> findByName(String name) throws JsonProcessingException {
        String formattedName = FormatingUserInputUtil.format(name);

        String request = createForGeocodingRequest(formattedName);
        String response = getDataByRequest(request);

        List<LocationRequestDto> locations = objectMapper
                .readValue(response, new TypeReference<>() {});

        if (locations.isEmpty()) {
            log.error("Location by name:{} not found", name);
            throw new NotFoundException("Locations by name: " + name + " not found");
        }
        return locations;
    }


    public List<WeatherResponseDto> getByLocations(List<LocationRequestDto> locations) throws JsonProcessingException {
        List<WeatherResponseDto> forecasts = new ArrayList<>();

        for (LocationRequestDto location : locations) {
            String request = createForWeatherRequest(location.getLat(), location.getLon());
            String response = getDataByRequest(request);

            WeatherRequestDto requestDto = objectMapper.readValue(response, WeatherRequestDto.class);

            WeatherResponseDto responseDto = MappingUtil.convertToDto(requestDto);
            forecasts.add(responseDto);
        }
        return forecasts;
    }


    public List<WeatherResponseDto>getByName(String name) throws JsonProcessingException {
        List<LocationRequestDto> locations = findByName(name);
        List<WeatherResponseDto> forecasts = getByLocations(locations);
        for (int i = 0; i < forecasts.size(); i++) {
            forecasts.get(i).setIndex(i);
        }

        return forecasts;
    }


    public String getDataByRequest(String request) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Objects> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(request, HttpMethod.GET, requestEntity, String.class);
        return responseEntity.getBody();
    }

    public WeatherResponseDto getByLocation(Location location){
        try {
            String request = createForWeatherRequest(location.getLat(), location.getLon());
            String response = getDataByRequest(request);

            WeatherRequestDto weatherRequest = objectMapper.readValue(response, WeatherRequestDto.class);

            return MappingUtil.convertToDto(weatherRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while parsing JSON", e);
        }
    }
}
