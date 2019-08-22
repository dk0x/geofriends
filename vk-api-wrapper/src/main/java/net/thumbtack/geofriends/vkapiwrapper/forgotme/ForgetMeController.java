package net.thumbtack.geofriends.vkapiwrapper.forgotme;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RestController
public class ForgetMeController {

    private ForgetMeService forgetMeService;

    @DeleteMapping("/api/vk/forgetMe")
    public void forgetMe(@CookieValue(VkApiConfig.SESSION_COOKIE_NAME) String sessionId) {
        log.debug("Enter in ForgetMeController.forgetMe(sessionId = {})", sessionId);

        forgetMeService.forgetMe(sessionId);

        log.debug("Exit from ForgetMeController.forgetMe()");
    }
}
