package net.thumbtack.geofriends.googleearthapiwrapper.geocode;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.googleearthapiwrapper.shared.GoogleEarthApiConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@AllArgsConstructor
@Slf4j
public class GeocodeProvider {
    private GoogleEarthApiConfig config;

    public GeocodingResult[] getCoordinatesByCountryAndCity(String country, String city) throws ApiException {
        log.debug("Enter in GeocodeProvider.geocodeByCountryAndCityName(country = {}, city = {})", country, city);

        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(config.getApiKey()).build();
        GeocodingResult[] geocodingResults = new GeocodingResult[0];
        try {
            geocodingResults = GeocodingApi.geocode(geoApiContext, country + ", " + city).await();
        } catch (InterruptedException | IOException ignored) {
        }

        log.debug("Exit from GeocodeProvider.geocodeByCountryAndCityName() with return {}", Arrays.toString(geocodingResults));
        return geocodingResults;
    }
}
