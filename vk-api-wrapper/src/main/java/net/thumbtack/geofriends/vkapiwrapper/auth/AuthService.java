package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private VkOAuthProvider vkOAuthProvider;
    private SessionRepository sessionRepository;

    public Session authByCode(String code) throws ClientException, ApiException {
        log.debug("Enter in AuthService.authByCode(code = {})", code);

        String accessToken = vkOAuthProvider.exchangeCodeForAccessToken(code);
        Session session = createAndSaveSession(accessToken);

        log.debug("Exit from AuthService.authByCode() with return {}", session);
        return session;
    }

    private Session createAndSaveSession(String accessToken) {
        log.debug("Enter in AuthService.createAndSaveSession(accessToken = {})", accessToken);

        Session session = new Session(generateSessionId(), accessToken);
        sessionRepository.save(session);

        log.debug("Exit from AuthService.createAndSaveSession() with return {}", session);
        return session;
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
