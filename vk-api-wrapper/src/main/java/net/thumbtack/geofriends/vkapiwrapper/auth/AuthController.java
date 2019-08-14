package net.thumbtack.geofriends.vkapiwrapper.auth;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class AuthController {
    private final static Logger log = LoggerFactory.getLogger(AuthController.class);

    private AuthService authService;

    @PostMapping("/vk/auth")
    public void auth(@RequestParam String code, HttpServletResponse httpServletResponse) throws AuthCodeInvalidException {
        log.debug("Enter in AuthController.auth(code = {})", code);

        Session session = authService.authByCode(code);
        Cookie cookie = new Cookie(Config.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        log.debug("Exit from AuthController.auth()");
    }
}
