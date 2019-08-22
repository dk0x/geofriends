package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.Fields;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.auth.Session;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionRepository;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FriendsService {
    private VkApiClient vkApiClient;
    private VkApiConfig vkApiConfig;
    private SessionRepository sessionRepository;

    public List<PersonDtoResponse> getFriends(String sessionId) throws SessionNotFoundException, ClientException, ApiException {
        log.debug("Enter in FriendsService.getFriends(sessionId = {})", sessionId);

        List<UserXtrLists> vkFriends = fetchFriendsByVkApi(getSessionById(sessionId).getAccessToken());
        List<PersonDtoResponse> convertedFriends = convertFriendsToPersons(vkFriends);

        log.debug("Exit from FriendsService.getFriends() with return list size {}", convertedFriends.size());
        return convertedFriends;
    }

    private Session getSessionById(String sessionId) throws SessionNotFoundException {
        log.debug("Enter in FriendsService.getSessionById(sessionId = {})", sessionId);

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (!optionalSession.isPresent()) {
            SessionNotFoundException sessionNotFoundException = new SessionNotFoundException();
            log.debug("Exit from FriendsService.getSessionById() by throwing {} with message {}", sessionNotFoundException.getClass().getName(), sessionNotFoundException.getMessage());
            throw sessionNotFoundException;
        }
        Session session = optionalSession.get();

        log.debug("Exit from FriendsService.getSessionById() with return {}", session);
        return session;
    }

    private List<UserXtrLists> fetchFriendsByVkApi(String accessToken) throws ClientException, ApiException {
        log.debug("Enter in FriendsService.fetchFriends(accessToken = {})", accessToken);

        List<UserXtrLists> vkFriends = vkApiClient
                .friends()
                .getWithFields(new UserActor(vkApiConfig.getAppId(), accessToken), Fields.CITY, Fields.COUNTRY, Fields.PHOTO_50)
                .execute()
                .getItems();

        log.debug("Exit from FriendsService.fetchFriends() with return list size {} ", vkFriends.size());
        return vkFriends;
    }

    private List<PersonDtoResponse> convertFriendsToPersons(List<UserXtrLists> input) {
        return input.stream().map(PersonDtoResponse::createFromUserXtrLists).collect(Collectors.toList());
    }
}
