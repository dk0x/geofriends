package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {
    private AuthService authService;

    @PostMapping("/api/vk/auth")
    public void auth(@RequestParam String code,
                     HttpServletRequest request,
                     HttpServletResponse response) throws ClientException, ApiException {
        log.debug("Enter in AuthController.auth(code = {})", code);

        Session session = authService.authByCode(code, buildURIFromRequest(request));
        Cookie cookie = new Cookie(VkApiConfig.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        response.addCookie(cookie);

        log.debug("Exit from AuthController.auth()");
    }

    private String buildURIFromRequest(HttpServletRequest request) {
        try {
            URIBuilder uriBuilder = new URIBuilder()
                    .setScheme(request.getScheme())
                    .setHost(request.getServerName());
            if (request.getServerPort() != 80 && request.getServerPort() != 443) {
                uriBuilder.setPort(request.getServerPort());
            }
            return uriBuilder.build().toString();
        } catch (URISyntaxException ignored) {
        }
        return "";
    }
}
