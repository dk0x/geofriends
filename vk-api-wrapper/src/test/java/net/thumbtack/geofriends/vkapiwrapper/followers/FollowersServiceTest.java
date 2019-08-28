package net.thumbtack.geofriends.vkapiwrapper.followers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.UserFull;
import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.shared.Person;
import net.thumbtack.geofriends.vkapiwrapper.shared.Session;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionExpiredException;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FollowersServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VkFollowersProvider provider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFollowers_whenCorrect_mustReturnPeopleConvertedFromVkFollowers() throws ClientException, ApiException, MalformedURLException, SessionExpiredException {
        Optional<Session> sessionOpt = Optional.of(new Session(TestHelper.TEST_SESSION_ID, TestHelper.TEST_ACCESS_TOKEN));
        when(sessionRepository.findById(anyString())).thenReturn(sessionOpt);
        List<UserFull> vkFollowers = Collections.singletonList(createTestVkFollower());
        when(provider.fetchVkFollowers(any())).thenReturn(vkFollowers);
        FollowersService service = new FollowersService(provider, sessionRepository);

        List<Person> persons = service.getFollowers(TestHelper.TEST_SESSION_ID);

        assertThat(persons).
                hasSize(1).
                contains(Person.createFromUserFull(vkFollowers.get(0)));
    }

    private UserFull createTestVkFollower() throws MalformedURLException {
        UserFull user = new UserXtrLists();
        user.setId(1);
        user.setFirstName("fn");
        user.setLastName("ln");
        BaseObject city = new BaseObject();
        city.setId(2);
        city.setTitle("city");
        user.setCity(city);
        Country country = new Country();
        country.setId(3);
        country.setTitle("country");
        user.setCountry(country);
        user.setPhoto50(new URL("http://photoUrl"));
        return user;
    }
}