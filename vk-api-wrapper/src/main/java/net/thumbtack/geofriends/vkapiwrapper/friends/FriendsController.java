package net.thumbtack.geofriends.vkapiwrapper.friends;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class FriendsController {
    private final static Logger log = LoggerFactory.getLogger(FriendsController.class);

    private FriendsService friendsService;

    @GetMapping("/api/vk/friends")
    public List<PersonDtoResponse> friends(@CookieValue(VkApiConfig.SESSION_COOKIE_NAME) String sessionId) throws SessionNotFoundException, ClientException, ApiException {
        log.debug("Enter in FriendsController.friends(sessionId = {})", sessionId);

        List<PersonDtoResponse> friends = friendsService.getFriends(sessionId);

        log.debug("Exit from FriendsController.friends() with return list size {}", friends.size());
        return friends;
    }
}
