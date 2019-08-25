package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.errors.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.googleearthapiwrapper.city.City;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class GeocodeController {
    private GeocodeService geocodeService;

    @PostMapping("/api/geo/geocode")
    public List<City> geocode(@RequestBody List<City> cities) throws ApiException {
        log.debug("Enter in GeocodeController.geocode(cities = {})", cities);

        List<City> citiesWithCoordinates = geocodeService.geocode(cities);

        log.debug("Exit from GeocodeController.geocode() with return {}", citiesWithCoordinates);
        return citiesWithCoordinates;
    }
}
