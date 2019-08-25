package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import net.thumbtack.geofriends.vkapiwrapper.TestHelper;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionExpiredException;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FriendsServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VkFriendsProvider vkFriendsProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFriends_whenCorrect_mustReturnPeopleConvertedFromVkFriends() throws ClientException, ApiException, MalformedURLException, SessionExpiredException {
        Optional<Session> sessionOpt = Optional.of(new Session(TestHelper.TEST_SESSION_ID, TestHelper.TEST_ACCESS_TOKEN));
        when(sessionRepository.findById(anyString())).thenReturn(sessionOpt);
        List<UserXtrLists> vkFriends = Collections.singletonList(createTestVkFriend());
        when(vkFriendsProvider.fetchVkFriends(any())).thenReturn(vkFriends);
        FriendsService friendsService = new FriendsService(vkFriendsProvider, sessionRepository);

        List<PersonDtoResponse> persons = friendsService.getFriends(TestHelper.TEST_SESSION_ID);

        assertThat(persons).
                hasSize(1).
                contains(PersonDtoResponse.createFromUserXtrLists(vkFriends.get(0)));
    }

    private UserXtrLists createTestVkFriend() throws MalformedURLException {
        UserXtrLists user = new UserXtrLists();
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