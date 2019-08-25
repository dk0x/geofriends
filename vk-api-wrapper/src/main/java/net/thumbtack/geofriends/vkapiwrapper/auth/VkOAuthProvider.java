package net.thumbtack.geofriends.vkapiwrapper.auth;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.VkApiConfig;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class VkOAuthProvider {
    private VkApiClient client;
    private VkApiConfig config;

    public String exchangeCodeForAccessToken(String code) throws ClientException, ApiException {
        log.debug("Enter in VkOAuthProvider.exchangeCodeForAccessToken(code = {})", code);

        String accessToken = client
                .oAuth()
                .userAuthorizationCodeFlow(config.getAppId(), config.getClientSecret(), config.getAuthorizeRedirectUri(), code)
                .execute()
                .getAccessToken();

        log.debug("Exit from VkOAuthProvider.exchangeCodeForAccessToken() with return {}", accessToken);
        return accessToken;
    }
}
