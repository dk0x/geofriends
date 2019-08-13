package net.thumbtack.geofriends.controller;

import net.thumbtack.geofriends.dto.response.PersonInfoDtoResponse;
import net.thumbtack.geofriends.exceptions.AuthenticationBrokenException;
import net.thumbtack.geofriends.exceptions.OauthCodeInvalidException;
import net.thumbtack.geofriends.exceptions.SessionNotFoundException;
import net.thumbtack.geofriends.model.AuthSession;
import net.thumbtack.geofriends.service.VkApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class VkApiController {
    private final static Logger LOGGER = LoggerFactory.getLogger(VkApiController.class);
    private final static String SESSION_COOKIE_NAME = "vkSessionId";

    private VkApiService vkApiService;

    @Autowired
    public VkApiController(VkApiService vkApiService) {
        this.vkApiService = vkApiService;
    }

    @GetMapping("/vk/auth")
    public String auth(@RequestParam String code, HttpServletResponse httpServletResponse)
            throws OauthCodeInvalidException {
        LOGGER.debug("Enter in VkApiController.auth(code = {})", code);
        final AuthSession authSession = vkApiService.authByCode(code);
        final String sessionId = authSession.getSessionId();
        final Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        LOGGER.debug("Exit from VkApiController.auth() with return {}", sessionId);
        return sessionId;
    }

    @GetMapping("/vk/getFriends")
    public List<PersonInfoDtoResponse> getFriends(@CookieValue(SESSION_COOKIE_NAME) String sessionId)
            throws SessionNotFoundException, AuthenticationBrokenException {
        LOGGER.debug("Enter in VkApiController.getFriends(sessionId = {})", sessionId);
        final List<PersonInfoDtoResponse> friends = vkApiService.getFriends(sessionId);
        LOGGER.debug("Exit from VkApiController.getFriends() with return {}", friends);
        return friends;
    }
}
