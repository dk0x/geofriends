package net.thumbtack.geofriends.vkapiwrapper.followers;

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
public class FollowersController {
    private FollowersService followersService;

    @GetMapping("/api/vk/followers")
    public List<Person> followers(@CookieValue(VkApiConfig.SESSION_COOKIE_NAME) String sessionId) throws SessionExpiredException, ClientException, ApiException {
        log.debug("Enter in FollowersController.followers(sessionId = {})", sessionId);

        List<Person> people = followersService.getFollowers(sessionId);

        log.debug("Exit from FollowersController.followers() with return list size {}", people.size());
        return people;
    }

}
