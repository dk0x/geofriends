package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.UserFull;
import lombok.Data;

@Data
public class Person {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final Integer cityId;
    private final String city;
    private final Integer countryId;
    private final String country;
    private final String photoUri;

    public static Person createFromUserFull(UserFull user) {
        BaseObject city = user.getCity();
        Country country = user.getCountry();
        Integer cityId = city != null ? city.getId() : -1;
        String cityTitle = city != null ? city.getTitle() : "";
        Integer countryId = country != null ? country.getId() : -1;
        String countryTitle = country != null ? country.getTitle() : "";

        return new Person(user.getId(), user.getFirstName(), user.getLastName(), cityId, cityTitle,
                countryId, countryTitle, user.getPhoto50().toString());
    }

    public static Person createFromUserXtrLists(UserXtrLists user) {
        BaseObject city = user.getCity();
        Country country = user.getCountry();
        Integer cityId = city != null ? city.getId() : -1;
        String cityTitle = city != null ? city.getTitle() : "";
        Integer countryId = country != null ? country.getId() : -1;
        String countryTitle = country != null ? country.getTitle() : "";

        return new Person(user.getId(), user.getFirstName(), user.getLastName(), cityId, cityTitle,
                countryId, countryTitle, user.getPhoto50().toString());
    }
}
