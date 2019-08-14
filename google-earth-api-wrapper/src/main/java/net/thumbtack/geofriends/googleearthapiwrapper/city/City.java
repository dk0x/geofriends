package net.thumbtack.geofriends.googleearthapiwrapper.city;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@DynamoDBTable(tableName = "City")
public class City {
    @DynamoDBHashKey
    private Integer id;
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    @DynamoDBIgnore
    public void copyCoordinates(City city) {
        latitude = city.getLatitude();
        longitude = city.getLongitude();
    }

    @DynamoDBIgnore
    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @DynamoDBIgnore
    public String getAddress() {
        return country + ", " + name;
    }
}
