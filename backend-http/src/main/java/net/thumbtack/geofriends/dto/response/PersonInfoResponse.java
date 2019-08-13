package net.thumbtack.geofriends.dto.response;

//todo lombok dis class
public class PersonInfoResponse {
    private final Integer vkAccountId;
    private final String firstName;
    private final String lastName;
    private final String city;
    private final String country;
    private final Double latitude;
    private final Double longitude;
    private final String photoUri;

    public PersonInfoResponse(Integer vkAccountId,
                              String firstName,
                              String lastName,
                              String city, String country,
                              Double latitude,
                              Double longitude,
                              String photoUri) {
        this.vkAccountId = vkAccountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoUri = photoUri;
    }

    public Integer getVkAccountId() {
        return vkAccountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
