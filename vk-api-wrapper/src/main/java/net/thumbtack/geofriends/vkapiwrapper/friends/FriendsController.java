package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.Person;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionExpiredException;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class FriendsController {
    private FriendsService friendsService;

    @GetMapping("/api/vk/friends")
    public List<Person> friends(@CookieValue(VkApiConfig.SESSION_COOKIE_NAME) String sessionId) throws SessionExpiredException, ClientException, ApiException {
        log.debug("Enter in FriendsController.friends(sessionId = {})", sessionId);

        List<Person> friends = friendsService.getFriends(sessionId);

        log.debug("Exit from FriendsController.friends() with return list size {}", friends.size());
        return friends;
    }
}
