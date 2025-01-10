//package com.weather_tracker.weather.location;
//
//import com.weather_tracker.commons.config.TestConfig;
//import com.weather_tracker.commons.config.WebAppInitializer;
//import com.weather_tracker.commons.exception.NotFoundException;
//import com.weather_tracker.weather.openWeatherApi.OpenWeatherMapApiService;
//import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ContextConfiguration(classes = {TestConfig.class, WebAppInitializer.class})
//public class LocationServiceMockTest {
//
//    @Mock
//    private LocationDao locationDao;
//
//    @Mock
//    private OpenWeatherMapApiService openWeatherMapApiService;
//    @Mock
//    private LocationRequestDto locationRequestDto1;
//    @Mock
//    private LocationRequestDto locationRequestDto2;
//    @InjectMocks
//    private LocationService locationService;
//    @Mock
//    private Location mocklocation1;
//    @Mock
//    private Location mocklocation2;
//    private Location location1;
//    private Location location2;
//    private final double lat1 = 59.9387;
//    private final double lon1 = 30.3162;
//    private final double lat2 = 54.6296;
//    private final double lon2 = 39.7425;
//
//    @BeforeAll
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        String name1 = "Sant Peterburg";
//        String name2 = "Ryazan";
//// behavior for locationRequestDto1
//        when(locationRequestDto1.getName()).thenReturn(name1);
//        when(locationRequestDto1.getLat()).thenReturn(lat1);
//        when(locationRequestDto1.getLon()).thenReturn(lon1);
//// behavior for locationRequestDto2
//        when(locationRequestDto2.getName()).thenReturn(name2);
//        when(locationRequestDto2.getLat()).thenReturn(lat2);
//        when(locationRequestDto2.getLon()).thenReturn(lon2);
//// behavior for locationDao
//        location1 = locationService.save(locationRequestDto1);
//        when(locationDao.save(location1)).thenReturn(mocklocation1);
//
//        location2 = locationService.save(locationRequestDto2);
//        when(locationDao.save(mocklocation2)).thenReturn(mocklocation2);
//    }
//
//    @Test
//    void shouldSaveLocation() {
//        assertEquals("Sant Peterburg", location1.getName());
//        assertEquals(lat1, location1.getLat());
//        assertEquals(lon1, location1.getLon());
//    }
//
//
//    @Test
//    void shouldDeleteLocation() {
//        locationService.delete(locationRequestDto1);
//        doThrow(NotFoundException.class).when(locationDao).delete(mocklocation1);
//    }
//
//
//    @Test
//    void shouldGetAllSavedLocations() {
//        List<Location> locations = new ArrayList<>();
//        locations.add(location1);
//        locations.add(location2);
//
//        ViewWeatherDto forecast1 = mock(ViewWeatherDto.class);
//        ViewWeatherDto forecast2 = mock(ViewWeatherDto.class);
//// behavior for forecast1
//        when(forecast1.getName()).thenReturn("Sant Peterburg");
//        when(forecast1.getLat()).thenReturn(lat1);
//        when(forecast1.getLon()).thenReturn(lon1);
//// behavior for forecast1
//        when(forecast2.getName()).thenReturn("Ryazan");
//        when(forecast2.getLat()).thenReturn(lat2);
//        when(forecast2.getLon()).thenReturn(lon2);
//// behavior for openWeatherMapApiService
//        when(openWeatherMapApiService.getByLocation(location1)).thenReturn(forecast1);
//        when(openWeatherMapApiService.getByLocation(location2)).thenReturn(forecast2);
//
//        List<ViewWeatherDto> savedLocations = locationService.getSavedForecasts(locations);
//
//        assertEquals(2, savedLocations.size());
//        assertEquals("Sant Peterburg", savedLocations.get(0).getName());
//        assertEquals("Ryazan", savedLocations.get(1).getName());
//        assertEquals(lat1, savedLocations.get(0).getLat());
//        assertEquals(lon1, savedLocations.get(0).getLon());
//        assertEquals(lat2, savedLocations.get(1).getLat());
//        assertEquals(lon2, savedLocations.get(1).getLon());
//
//        verify(openWeatherMapApiService, times(1)).getByLocation(location1);
//        verify(openWeatherMapApiService, times(1)).getByLocation(location2);
//    }
//}
