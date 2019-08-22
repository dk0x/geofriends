package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.actions.Friends;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.base.BaseObject;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.friends.responses.GetFieldsResponse;
import com.vk.api.sdk.queries.friends.FriendsGetQueryWithFields;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FriendsServiceTest {

    private final static String TEST_SESSION_ID = "sessionId";
    private final static String TEST_ACCESS_TOKEN = "accessToken";
    @Mock
    private VkApiConfig vkApiConfig;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private VkApiClient vkApiClient;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFriends_whenCorrect_mustReturnPeopleConvertedFromFriends() throws ClientException, ApiException, MalformedURLException, SessionNotFoundException {
        Session session = new Session(TEST_SESSION_ID, TEST_ACCESS_TOKEN);
        Optional<Session> sessionOpt = Optional.of(session);
        when(sessionRepository.findById(anyString())).thenReturn(sessionOpt);

        Friends friends = mock(Friends.class);
        when(vkApiClient.friends()).thenReturn(friends);
        FriendsGetQueryWithFields queryWithFields = mock(FriendsGetQueryWithFields.class);
        when(friends.getWithFields(any(UserActor.class), any(), any(), any())).thenReturn(queryWithFields);
        GetFieldsResponse response = mock(GetFieldsResponse.class);
        when(queryWithFields.execute()).thenReturn(response);

        ArrayList<UserXtrLists> users = new ArrayList<>();
        UserXtrLists userFromVkApi = createVkUser();
        users.add(userFromVkApi);
        when(response.getItems()).thenReturn(users);

        FriendsService friendsService = new FriendsService(vkApiClient, vkApiConfig, sessionRepository);
        List<PersonDtoResponse> persons = friendsService.getFriends(TEST_SESSION_ID);

        assertThat(persons).
                hasSize(1).
                contains(PersonDtoResponse.createFromUserXtrLists(userFromVkApi));
    }

    private UserXtrLists createVkUser() throws MalformedURLException {
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