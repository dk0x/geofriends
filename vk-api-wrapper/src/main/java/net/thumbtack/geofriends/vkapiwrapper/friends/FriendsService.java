package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.friends.responses.GetFieldsResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.queries.friends.FriendsGetQueryWithFields;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.AuthenticationBrokenException;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
import net.thumbtack.geofriends.vkapiwrapper.shared.Session;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendsService {

    private Config config;
    private SessionRepository sessionRepository;

    public List<PersonDtoResponse> getFriends(String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        Session session = getSessionById(sessionId);
        List<UserXtrLists> friends = fetchFriends(session.getAccessToken());

        return convertFriendsToPersons(friends);
    }

    private Session getSessionById(String sessionId) throws SessionNotFoundException {
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (!optionalSession.isPresent())
            throw new SessionNotFoundException();
        return optionalSession.get();
    }

    private List<UserXtrLists> fetchFriends(String accessToken) throws AuthenticationBrokenException {
        VkApiClient vkApiClient = new VkApiClient(HttpTransportClient.getInstance());
        UserActor actor = new UserActor(config.getAppId(), accessToken);
        FriendsGetQueryWithFields query = vkApiClient.friends().getWithFields(actor, Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50);

        GetFieldsResponse response;
        try {
            response = query.execute();
        } catch (ApiException | ClientException e) {
            throw new AuthenticationBrokenException(e);
        }
        return response.getItems();
    }

    private List<PersonDtoResponse> convertFriendsToPersons(List<UserXtrLists> input) {
        return input.stream().map(PersonDtoResponse::createFromUserXtrLists).collect(Collectors.toList());
    }
}
