package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class VkOAuthProvider {
    private VkApiClient vkApiClient;

    public String exchangeCodeForAccessToken(String code,
                                             int clientId,
                                             String clientSecret,
                                             String redirectUri) throws ClientException, ApiException {
        log.debug("Enter in VkOAuthProvider.exchangeCodeForAccessToken(code = {}, clientId = {}, clientSecret = {}, redirectUri = {})", code, clientId, clientSecret, redirectUri);

        String accessToken = vkApiClient
                .oAuth()
                .userAuthorizationCodeFlow(clientId, clientSecret, redirectUri, code)
                .execute()
                .getAccessToken();

        log.debug("Exit from VkOAuthProvider.exchangeCodeForAccessToken() with return {}", accessToken);
        return accessToken;
    }
}
