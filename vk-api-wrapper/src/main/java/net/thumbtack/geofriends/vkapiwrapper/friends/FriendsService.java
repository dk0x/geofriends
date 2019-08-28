package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.Person;
import net.thumbtack.geofriends.vkapiwrapper.shared.Session;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionExpiredException;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FriendsService {
    private VkFriendsProvider vkFriendsProvider;
    private SessionRepository sessionRepository;

    public List<Person> getFriends(String sessionId) throws SessionExpiredException, ClientException, ApiException {
        log.debug("Enter in FriendsService.getFriends(sessionId = {})", sessionId);

        List<UserXtrLists> friendsInVkFormat = vkFriendsProvider.fetchVkFriends(getSessionById(sessionId).getAccessToken());
        List<Person> persons = convertVkFriendsToPersons(friendsInVkFormat);

        log.debug("Exit from FriendsService.getFriends() with return list size {}", persons.size());
        return persons;
    }

    private Session getSessionById(String sessionId) throws SessionExpiredException {
        log.debug("Enter in FriendsService.getSessionById(sessionId = {})", sessionId);

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (!optionalSession.isPresent()) {
            throw new SessionExpiredException();
        }
        Session session = optionalSession.get();

        log.debug("Exit from FriendsService.getSessionById() with return {}", session);
        return session;
    }

    private List<Person> convertVkFriendsToPersons(List<UserXtrLists> input) {
        return input.stream().map(Person::createFromUserXtrLists).collect(Collectors.toList());
    }
}
