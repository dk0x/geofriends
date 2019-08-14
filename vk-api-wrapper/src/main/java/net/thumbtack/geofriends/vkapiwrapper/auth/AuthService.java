package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.queries.oauth.OAuthUserAuthorizationCodeFlowQuery;
import lombok.AllArgsConstructor;
import net.thumbtack.geofriends.vkapiwrapper.shared.Config;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private Config config;
    private SessionRepository sessionRepository;

    public Session authByCode(String code) throws AuthCodeInvalidException {
        String accessToken = exchangeCodeForAccessToken(code);
        return createAndSaveSession(accessToken);
    }

    private String exchangeCodeForAccessToken(String code) throws AuthCodeInvalidException {
        VkApiClient vkApiClient = new VkApiClient(HttpTransportClient.getInstance());
        OAuthUserAuthorizationCodeFlowQuery query = vkApiClient.oAuth().userAuthorizationCodeFlow(
                config.getAppId(), config.getClientSecret(), config.getAuthorizeRedirectUri(), code);

        UserAuthResponse authResponse;
        try {
            authResponse = query.execute();
        } catch (ApiException | ClientException e) {
            throw new AuthCodeInvalidException(e);
            // TODO: catch ClientException with redirect url invalid in config
        }

        return authResponse.getAccessToken();
    }

    private Session createAndSaveSession(String accessToken) {
        Session session = new Session(generateSessionId(), accessToken);
        sessionRepository.save(session);
        return session;
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
