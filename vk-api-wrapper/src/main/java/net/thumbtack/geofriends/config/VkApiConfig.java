package net.thumbtack.geofriends.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkApiConfig {
    private int appId;
    private String clientSecret;
    private String authorizeRedirectUri;

    public VkApiConfig(@Value("${vkapi.app-id}") int appId, @Value("${vkapi.client-secret}") String clientSecret,
                       @Value("${vkapi.authorize-redirect-uri}") String authorizeRedirectUri) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.authorizeRedirectUri = authorizeRedirectUri;
    }

    public int getAppId() {
        return appId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthorizeRedirectUri() {
        return authorizeRedirectUri;
    }

}
