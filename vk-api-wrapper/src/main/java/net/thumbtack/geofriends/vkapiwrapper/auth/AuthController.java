package net.thumbtack.geofriends.vkapiwrapper.auth;

import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/vk/auth")
    public void auth(@RequestParam String code, HttpServletResponse httpServletResponse) throws AuthCodeInvalidException {
        Session session = authService.authByCode(code);
        Cookie cookie = new Cookie(Config.SESSION_COOKIE_NAME, session.getSessionId());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
