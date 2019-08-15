package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import net.thumbtack.geofriends.googleearthapiwrapper.city.CityRepository;
import net.thumbtack.geofriends.googleearthapiwrapper.shared.GoogleEarthApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GeocodeService {
    private final static Logger log = LoggerFactory.getLogger(GeocodeService.class);

    private GoogleEarthApiConfig config;
    private CityRepository repository;

    public List<City> geocode(List<City> cities) throws ApiException {
        log.debug("Enter in GeocodeService.geocode(cities = {})", cities);

        for (City city : cities) {
            Optional<City> optionalCity = repository.findById(city.getId());
            if (optionalCity.isPresent()) {
                city.copyCoordinates(optionalCity.get());
            } else {
                geocodeAndSave(city);
            }
        }

        log.debug("Exit from GeocodeService.geocode() with return list size {}", cities.size());
        return cities;
    }

    private void geocodeAndSave(City city) throws ApiException {
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(config.getApiKey()).build();

        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, city.getAddress()).await();
            if (results.length > 0) {
                city.setCoordinates(results[0].geometry.location.lat, results[0].geometry.location.lng);
                repository.save(city);
            }
        } catch (InterruptedException | IOException ignored) {
        }

    }
}
