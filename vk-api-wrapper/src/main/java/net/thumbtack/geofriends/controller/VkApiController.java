package net.thumbtack.geofriends.controller;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.dto.response.PersonInfoDtoResponse;
import net.thumbtack.geofriends.exceptions.AuthenticationBrokenException;
import net.thumbtack.geofriends.exceptions.OauthCodeInvalidException;
import net.thumbtack.geofriends.exceptions.SessionNotFoundException;
import net.thumbtack.geofriends.model.AuthSession;
import net.thumbtack.geofriends.service.VkApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@AllArgsConstructor
public class VkApiController {
    private final static Logger log = LoggerFactory.getLogger(VkApiController.class);
    private final static String SESSION_COOKIE_NAME = "vkSessionId";

    private VkApiService vkApiService;

    @GetMapping("/vk/auth")
    public void auth(@RequestParam String code, HttpServletResponse httpServletResponse) throws OauthCodeInvalidException {
        log.debug("Enter in VkApiController.auth(code = {})", code);

        AuthSession session = vkApiService.authByCode(code);
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        log.debug("Exit from VkApiController.auth()");
    }

    @GetMapping("/vk/getFriends")
    public List<PersonInfoDtoResponse> getFriends(@CookieValue(SESSION_COOKIE_NAME) String sessionId) throws SessionNotFoundException, AuthenticationBrokenException {
        log.debug("Enter in VkApiController.getFriends(sessionId = {})", sessionId);
        final List<PersonInfoDtoResponse> friends = vkApiService.getFriends(sessionId);
        log.debug("Exit from VkApiController.getFriends() with return {}", friends);
        return friends;
    }
}
