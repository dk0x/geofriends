package net.thumbtack.geofriends.vkapiwrapper.friends;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.auth.AuthenticationBrokenException;
import net.thumbtack.geofriends.vkapiwrapper.auth.SessionNotFoundException;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
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

    @GetMapping("/vk/friends")
    public List<PersonDtoResponse> friends(@CookieValue(Config.SESSION_COOKIE_NAME) String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        log.debug("Enter in FriendsController.friends(sessionId = {})", sessionId);

        List<PersonDtoResponse> friends = friendsService.getFriends(sessionId);

        log.debug("Exit from FriendsController.friends() with return list size {}", friends.size());
        return friends;
    }
}
