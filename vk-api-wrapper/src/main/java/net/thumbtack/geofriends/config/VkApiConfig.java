package net.thumbtack.geofriends.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "vkapi")
@Setter
@Getter
public class VkApiConfig {
    private int appId;
    private String clientSecret;
    private String authorizeRedirectUri;
}
