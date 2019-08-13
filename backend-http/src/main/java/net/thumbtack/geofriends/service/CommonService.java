package net.thumbtack.geofriends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

//    private final static Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    public CommonService() {
    }

//    public List<PersonInfoResponse> getFriends(String code) {
//
//
//        final List<PersonInfoResponse> responses = new ArrayList<>();
//
//
//        final String googleApiKey = "";
//        final GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(googleApiKey).build();
//
//        for (VkPersonInfo outputFriend : outputFriends) {
//
//            final String address = outputFriend.getCountry() + ", " + outputFriend.getCity();
//            final GeocodingResult[] geocodingResults;
//            try {
//                geocodingResults = GeocodingApi.geocode(geoApiContext, address).await();
//            } catch (com.google.maps.errors.ApiException | InterruptedException | IOException e) {
//                continue;
//            }
//
//            double latitude = 0.0d;
//            double longitude = 0.0d;
//            for (GeocodingResult geocodingResult : geocodingResults) {
//                latitude = geocodingResult.geometry.location.lat;
//                longitude = geocodingResult.geometry.location.lng;
//            }
//
//            final PersonInfoResponse personInfoResponse =
//                    new PersonInfoResponse(outputFriend.getId(), outputFriend.getFirstName(),
//                            outputFriend.getLastName(), outputFriend.getCity(), outputFriend.getCountry(), latitude,
//                            longitude, outputFriend.getPhotoUri());
//
//            responses.add(personInfoResponse);
//
//        }
//
//        return responses;
//    }
}
