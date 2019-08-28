package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.Session;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
    public void authPost(@RequestParam String code,
                         HttpServletRequest request,
                         HttpServletResponse response) throws ClientException, ApiException {
        log.debug("Enter in AuthController.auth(code = {})", code);

        Session session = authService.authByCode(code, buildURIFromRequest(request, false));
        Cookie cookie = new Cookie(VkApiConfig.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        response.addCookie(cookie);

        log.debug("Exit from AuthController.auth()");
    }

    @GetMapping("/api/vk/auth")
    public RedirectView auth(@RequestParam String code,
                             HttpServletRequest request,
                             HttpServletResponse response) throws ClientException, ApiException {

        Session session = authService.authByCode(code, buildURIFromRequest(request, true));
        Cookie cookie = new Cookie(VkApiConfig.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        response.addCookie(cookie);

        return new RedirectView("/");
    }

    private String buildURIFromRequest(HttpServletRequest request, boolean includePath) {
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build();

            URIBuilder uriBuilder = new URIBuilder()
                    .setScheme(uriComponents.getScheme())
                    .setHost(uriComponents.getHost());
            if (uriComponents.getPort() != 80 && uriComponents.getPort() != 443) {
                uriBuilder.setPort(uriComponents.getPort());
            }
            if (includePath) {
                uriBuilder.setPath(uriComponents.getPath());
            }

            return uriBuilder.build().toString();
        } catch (URISyntaxException ignored) {
        }
        return "";
    }
}
