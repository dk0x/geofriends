package net.thumbtack.geofriends.dto.response;

import java.util.Objects;

public class PersonInfoDtoResponse {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final Integer cityId;
    private final String city;
    private final Integer countryId;
    private final String country;
    private final String photoUri;

    public PersonInfoDtoResponse(int id, String firstName, String lastName, Integer cityId, String city,
                                 Integer countryId, String country, String photoUri) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.country = country;
        this.photoUri = photoUri;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public String getCity() {
        return city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public String getCountry() {
        return country;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VkPersonInfoResponse{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", cityId=").append(cityId);
        sb.append(", city='").append(city).append('\'');
        sb.append(", countryId=").append(countryId);
        sb.append(", country='").append(country).append('\'');
        sb.append(", photoUri='").append(photoUri).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PersonInfoDtoResponse))
            return false;
        PersonInfoDtoResponse that = (PersonInfoDtoResponse) o;
        return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getCityId(), that.getCityId()) &&
                Objects.equals(getCity(), that.getCity()) && Objects.equals(getCountryId(), that.getCountryId()) &&
                Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getPhotoUri(), that.getPhotoUri());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(), getFirstName(), getLastName(), getCityId(), getCity(), getCountryId(), getCountry(),
                        getPhotoUri());
    }
}
