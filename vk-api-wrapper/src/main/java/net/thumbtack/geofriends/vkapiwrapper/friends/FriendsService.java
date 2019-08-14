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
import net.thumbtack.geofriends.vkapiwrapper.auth.AuthenticationBrokenException;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FriendsService {
    private final static Logger log = LoggerFactory.getLogger(FriendsService.class);

    private VkApiConfig vkApiConfig;
    private SessionRepository sessionRepository;

    public List<PersonDtoResponse> getFriends(String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        log.debug("Enter in FriendsService.getFriends(sessionId = {})", sessionId);

        List<UserXtrLists> vkFriends = fetchFriendsByVkApi(getSessionById(sessionId).getAccessToken());
        List<PersonDtoResponse> convertedFriends = convertFriendsToPersons(vkFriends);

        log.debug("Exit from FriendsService.getFriends() with return list size {}", convertedFriends.size());
        return convertedFriends;
    }

    private Session getSessionById(String sessionId) throws SessionNotFoundException {
        log.debug("Enter in FriendsService.getSessionById(sessionId = {})", sessionId);

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (!optionalSession.isPresent())
            throw new SessionNotFoundException();
        Session session = optionalSession.get();

        log.debug("Exit from FriendsService.getSessionById() with return {}", session);
        return session;
    }

    private List<UserXtrLists> fetchFriendsByVkApi(String accessToken) throws AuthenticationBrokenException {
        log.debug("Enter in FriendsService.fetchFriends(accessToken = {})", accessToken);

        FriendsGetQueryWithFields query = new VkApiClient(HttpTransportClient.getInstance())
                .friends()
                .getWithFields(new UserActor(vkApiConfig.getAppId(), accessToken), Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50);

        GetFieldsResponse response;
        try {
            response = query.execute();
        } catch (ApiException | ClientException e) {
            throw new AuthenticationBrokenException(e);
        }
        List<UserXtrLists> vkFriends = response.getItems();

        log.debug("Exit from FriendsService.fetchFriends() with return list size {} ", vkFriends.size());
        return vkFriends;
    }

    private List<PersonDtoResponse> convertFriendsToPersons(List<UserXtrLists> input) {
        return input.stream().map(PersonDtoResponse::createFromUserXtrLists).collect(Collectors.toList());
    }
}
