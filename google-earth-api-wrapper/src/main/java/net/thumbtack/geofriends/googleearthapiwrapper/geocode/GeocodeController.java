package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.errors.ApiException;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GeocodeController {
    private final static Logger log = LoggerFactory.getLogger(GeocodeController.class);

    private GeocodeService geocodeService;

    @PostMapping("/api/geo/geocode")
    public List<City> geocode(@RequestBody List<City> cities) throws ApiException {
        log.debug("Enter in GeocodeController.geocode(cities = {})", cities);

        List<City> geocodedCities = geocodeService.geocode(cities);

        log.debug("Exit from GeocodeController.geocode() with return {}", geocodedCities);
        return geocodedCities;
    }
}
