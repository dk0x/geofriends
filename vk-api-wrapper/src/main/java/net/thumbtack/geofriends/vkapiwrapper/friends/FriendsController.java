package net.thumbtack.geofriends.vkapiwrapper.friends;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.AuthenticationBrokenException;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
import net.thumbtack.geofriends.vkapiwrapper.shared.SessionNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class FriendsController {
    private FriendsService friendsService;

    @GetMapping("/vk/friends")
    public List<PersonDtoResponse> friends(@CookieValue(Config.SESSION_COOKIE_NAME) String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        return friendsService.getFriends(sessionId);
    }
}
