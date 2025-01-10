//package com.weather_tracker.weather.openWeatherApi;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.weather_tracker.commons.config.TestConfig;
//import com.weather_tracker.commons.config.WebAppInitializer;
//import com.weather_tracker.weather.location.Location;
//import com.weather_tracker.weather.location.LocationRequestDto;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@WebAppConfiguration
//public class OpenWeatherMapApiTest {
//
//    @Mock
//    Location location;
//    private final OpenWeatherMapApiService weatherApiService;
//    private String name;
//
//    @Autowired
//    public OpenWeatherMapApiTest(OpenWeatherMapApiService weatherApiService) {
//        this.weatherApiService = weatherApiService;
//    }
//
//    @BeforeAll
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        name = "Ryazan";
//    }
//
//
//    @Test
//    void shouldFindListOfLocationsByName() throws JsonProcessingException {
//
//        List<LocationRequestDto>  locations = weatherApiService.findByName(name);
//
//        assertNotNull(locations);
//        assertEquals(2, locations.size());
//        assertEquals(name, locations.get(0).getName());
//    }
//
//    @Test
//    void shouldReturnWeatherResponseDtoByLocation() {
//        double lat = 54.6296;
//        double lon = 39.7425;
//// behavior for location
//        when(location.getLat()).thenReturn(lat);
//        when(location.getLon()).thenReturn(lon);
//        when(location.getName()).thenReturn(name);
//
//        ViewWeatherDto result = weatherApiService.getByLocation(location);
//
//        assertNotNull(result);
//        assertEquals(result.getName(), location.getName());
//        assertEquals(result.getLat(), location.getLat());
//        assertEquals(result.getLon(), location.getLon());
//    }
//}
