package com.weather_tracker.weather.location;

import com.weather_tracker.auth.model.user.User;
import com.weather_tracker.commons.exception.DataBaseException;
import com.weather_tracker.commons.exception.InvalidParameterException;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiResponseDto;
import com.weather_tracker.weather.openWeatherApi.OpenWeatherApiService;
import com.weather_tracker.weather.openWeatherApi.ViewWeatherDto;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @InjectMocks
    private LocationService locationService;
    @Mock
    private LocationDao locationDao;
    @Mock
    private OpenWeatherApiService openWeatherApiService;
    String login = "SomeLogin";
    String locationName = "SomeName";
    Double lat = 20.00;
    Double lon = 30.00;
    User user = new User();
    Location testLocation = new Location(1, locationName,user,lat,lon);

    @Test
    void saveSuccessfully() {
        user.setLogin(login);
        LocationRequestDto testLocationDto = new LocationRequestDto();
        Mockito.when(locationDao.save(Mockito.any(Location.class))).thenReturn(testLocation);

        Location location = locationService.save(testLocationDto);

        Assertions.assertNotNull(location);
        Assertions.assertEquals(location.getName(), locationName);
        Assertions.assertEquals(location.getLat(), lat);
        Assertions.assertEquals(location.getLon(), lon);
        Assertions.assertEquals(location.getUser().getLogin(), login);

        Mockito.verify(locationDao, Mockito.times(1)).save(Mockito.any(Location.class));
    }

    @Test
    void shouldThrowInvalidParameterExceptionWhenSaveFails() {
        LocationRequestDto testLocationDto = new LocationRequestDto();
        Mockito.when(locationDao.save(Mockito.any(Location.class))).thenThrow(ConstraintViolationException.class);

        Assertions.assertThrows(InvalidParameterException.class, () -> locationService.save(testLocationDto));
    }


    @Test
    void deleteSuccessfully() {
        locationService.delete(user,lat,lon);
        Mockito.verify(locationDao, Mockito.times(1)).delete(user,lat,lon);

    }

    @Test
    void shouldThrowDataBaseExceptionWhenDeleteFails() {
        Mockito.doThrow(DataBaseException.class).when(locationDao).delete(user,lat,lon);
        Assertions.assertThrows(DataBaseException.class, () -> locationService.delete(user,lat,lon));
    }


    @Test
    void getByLocationsSuccessfully(){
        Location testLocation1 = new Location(1,"name1",user,10.00,20.00);
        Location testLocation2 = new Location(2,"name2",user,30.00,40.00);
        Location testLocation3 = new Location(3,"name3",user,50.00,60.00);

        List<Location> locations = new ArrayList<>();
        locations.add(testLocation1);
        locations.add(testLocation2);
        locations.add(testLocation3);

        OpenWeatherApiResponseDto testDto = new OpenWeatherApiResponseDto();
        testDto.setName(locationName);

        Mockito.when(openWeatherApiService.getByLocation(Mockito.any(Location.class))).thenReturn(testDto);

        List<ViewWeatherDto> result = locationService.getByLocations(locations);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("name1", result.get(0).getName());
        Assertions.assertEquals("name2", result.get(1).getName());
        Assertions.assertEquals("name3", result.get(2).getName());

        Mockito.verify(openWeatherApiService, Mockito.times(3)).getByLocation(Mockito.any(Location.class));
    }


    @Test
    void shouldThrowDataBaseExceptionWhenGetByLocationsFails() {
        List<Location> locations = new ArrayList<>();
        locations.add(testLocation);

        Mockito.when(openWeatherApiService.getByLocation((Mockito.any(Location.class)))).thenThrow(DataBaseException.class);
        Assertions.assertThrows(DataBaseException.class, () -> locationService.getByLocations(locations));
    }
}
