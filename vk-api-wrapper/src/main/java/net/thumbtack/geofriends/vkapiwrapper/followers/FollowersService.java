package net.thumbtack.geofriends.vkapiwrapper.followers;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
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
public class FollowersService {
    private VkFollowersProvider vkFollowersProvider;
    private SessionRepository sessionRepository;

    public List<Person> getFollowers(String sessionId) throws SessionExpiredException, ClientException, ApiException {
        log.debug("Enter in FollowersService.getFollowers(sessionId = {})", sessionId);

        List<UserFull> vkPersons = vkFollowersProvider.fetchVkFollowers(getSessionById(sessionId).getAccessToken());
        List<Person> persons = convertVkPersonsToPersons(vkPersons);

        log.debug("Exit from FollowersService.getFollowers() with return list size {}", persons.size());
        return persons;
    }

    private Session getSessionById(String sessionId) throws SessionExpiredException {
        log.debug("Enter in FollowersService.getSessionById(sessionId = {})", sessionId);

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (!optionalSession.isPresent()) {
            throw new SessionExpiredException();
        }
        Session session = optionalSession.get();

        log.debug("Exit from FollowersService.getSessionById() with return {}", session);
        return session;
    }

    private List<Person> convertVkPersonsToPersons(List<UserFull> input) {
        return input.stream().map(Person::createFromUserFull).collect(Collectors.toList());
    }
}
