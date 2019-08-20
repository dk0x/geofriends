package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {
    private AuthService authService;

    @PostMapping("/api/vk/auth")
    public void auth(@RequestParam String code, HttpServletResponse httpServletResponse) throws ClientException, ApiException {
        log.debug("Enter in AuthController.auth(code = {})", code);

        Session session = authService.authByCode(code);
        Cookie cookie = new Cookie(VkApiConfig.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        log.debug("Exit from AuthController.auth()");
    }
}
