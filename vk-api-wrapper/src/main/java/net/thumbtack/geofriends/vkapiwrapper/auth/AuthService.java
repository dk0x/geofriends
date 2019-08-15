package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final static Logger log = LoggerFactory.getLogger(AuthService.class);

    private VkApiConfig vkApiConfig;
    private VkApiClient vkApiClient;
    private SessionRepository sessionRepository;

    public Session authByCode(String code) throws ClientException, ApiException {
        log.debug("Enter in AuthService.authByCode(code = {})", code);

        String accessToken = exchangeCodeForAccessToken(code);
        Session session = createAndSaveSession(accessToken);

        log.debug("Exit from AuthService.authByCode() with return {}", session);
        return session;
    }

    private String exchangeCodeForAccessToken(String code) throws ClientException, ApiException {
        log.debug("Enter in AuthService.exchangeCodeForAccessToken(code = {})", code);

        String accessToken = vkApiClient
                .oAuth()
                .userAuthorizationCodeFlow(vkApiConfig.getAppId(), vkApiConfig.getClientSecret(), vkApiConfig.getAuthorizeRedirectUri(), code)
                .execute()
                .getAccessToken();

        log.debug("Exit from AuthService.exchangeCodeForAccessToken() with return {}", accessToken);
        return accessToken;
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
