package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import net.thumbtack.geofriends.googleearthapiwrapper.city.CityRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeocodeServiceTest {
    private static final double TEST_LAT = 20;
    private static final double TEST_LNG = 30;

    private GeocodeProvider geocodeProvider;
    private CityRepository cityRepository;

    @Before
    public void setUp() {
        geocodeProvider = mock(GeocodeProvider.class);
        cityRepository = mock(CityRepository.class);
    }

    @Test
    public void geocode_whenCityNotCached_mustCallApiForCoordinates() throws ApiException {
        when(geocodeProvider.getCoordinatesByCountryAndCity(eq("name"), eq("country")))
                .thenReturn(makeGeocodingResults(TEST_LAT, TEST_LNG));
        City cityRequested = new City(1, "name", "country", 2, 3);
        GeocodeService geocodeService = new GeocodeService(geocodeProvider, cityRepository);

        geocodeService.geocode(Collections.singletonList(cityRequested));

        verify(geocodeProvider).getCoordinatesByCountryAndCity(eq("name"), eq("country"));
    }

    @Test
    public void geocode_whenCityNotCached_mustSetCorrectCoordinatesFromApi() throws ApiException {
        when(geocodeProvider.getCoordinatesByCountryAndCity(eq("name"), eq("country")))
                .thenReturn(makeGeocodingResults(TEST_LAT, TEST_LNG));
        City cityRequested = new City(1, "name", "country", 2, 3);
        GeocodeService geocodeService = new GeocodeService(geocodeProvider, cityRepository);

        List<City> citiesWithCoordinates = geocodeService.geocode(Collections.singletonList(cityRequested));

        assertThat(citiesWithCoordinates).hasSize(1);
        City actualCity = citiesWithCoordinates.get(0);
        assertThat(actualCity.getId()).as("check city id").isEqualTo(cityRequested.getId());
        assertThat(actualCity.getName()).as("check city name").isEqualTo(cityRequested.getName());
        assertThat(actualCity.getCountry()).as("check city country").isEqualTo(cityRequested.getCountry());
        assertThat(actualCity.getLatitude()).as("check city lat").isEqualTo(TEST_LAT);
        assertThat(actualCity.getLongitude()).as("check city lng").isEqualTo(TEST_LNG);
    }

    @Test
    public void geocode_whenCityCached_mustSetCorrectCoordinatesFromStorage() throws ApiException {
        City cityRequested = new City(1, "name", "country", 2, 3);
        GeocodeService geocodeService = new GeocodeService(geocodeProvider, cityRepository);
        City cityInStorage = new City(1, "name", "country", TEST_LAT, TEST_LNG);
        when(cityRepository.findById(eq(cityRequested.getId()))).thenReturn(Optional.of(cityInStorage));

        List<City> citiesWithCoordinates = geocodeService.geocode(Collections.singletonList(cityRequested));

        assertThat(citiesWithCoordinates).hasSize(1);
        City actualCity = citiesWithCoordinates.get(0);
        assertThat(actualCity.getId()).as("check city id").isEqualTo(cityRequested.getId());
        assertThat(actualCity.getName()).as("check city name").isEqualTo(cityRequested.getName());
        assertThat(actualCity.getCountry()).as("check city country").isEqualTo(cityRequested.getCountry());
        assertThat(actualCity.getLatitude()).as("check city lat").isEqualTo(TEST_LAT);
        assertThat(actualCity.getLongitude()).as("check city lng").isEqualTo(TEST_LNG);
    }

    private GeocodingResult[] makeGeocodingResults(double lat, double lng) {
        GeocodingResult geocodingResult = new GeocodingResult();
        geocodingResult.geometry = new Geometry();
        geocodingResult.geometry.location = new LatLng();
        geocodingResult.geometry.location.lat = lat;
        geocodingResult.geometry.location.lng = lng;
        return new GeocodingResult[]{geocodingResult};
    }
}