package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import net.thumbtack.geofriends.googleearthapiwrapper.city.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GeocodeService {
    private GeocodeProvider geocodeProvider;
    private CityRepository repository;

    public List<City> geocode(List<City> cities) throws ApiException {
        log.debug("Enter in GeocodeService.geocode(cities = {})", cities);

        for (City city : cities) {
            Optional<City> optionalCity = repository.findById(city.getId());
            if (optionalCity.isPresent()) {
                city.copyCoordinates(optionalCity.get());
            } else {
                geocodeCityAndSaveToStorage(city);
            }
        }

        log.debug("Exit from GeocodeService.geocode() with return list size {}", cities.size());
        return cities;
    }

    private void geocodeCityAndSaveToStorage(City city) throws ApiException {
        GeocodingResult[] results = geocodeProvider.getCoordinatesByCountryAndCity(city.getName(), city.getCountry());
        if (results.length > 0) {
            city.setCoordinates(results[0].geometry.location.lat, results[0].geometry.location.lng);
            repository.save(city);
        }
    }
}
