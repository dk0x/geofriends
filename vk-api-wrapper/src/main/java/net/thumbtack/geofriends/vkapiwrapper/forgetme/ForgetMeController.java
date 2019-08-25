package net.thumbtack.geofriends.vkapiwrapper.forgetme;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@Slf4j
@RestController
public class ForgetMeController {

    private ForgetMeService forgetMeService;

    @DeleteMapping("/api/vk/forgetMe")
    public void forgetMe(
            @CookieValue(value = VkApiConfig.SESSION_COOKIE_NAME, required = false) String sessionId,
            HttpServletResponse httpServletResponse) {
        log.debug("Enter in ForgetMeController.forgetMe(sessionId = {})", sessionId);

        forgetMeService.forgetMe(sessionId);

        Cookie cookie = new Cookie(VkApiConfig.SESSION_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);

        log.debug("Exit from ForgetMeController.forgetMe()");
    }
}
