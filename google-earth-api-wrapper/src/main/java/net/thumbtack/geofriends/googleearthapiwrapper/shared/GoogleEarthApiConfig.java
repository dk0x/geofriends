package net.thumbtack.geofriends.googleearthapiwrapper.shared;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "google-earth-api")
@Setter
@Getter
public class GoogleEarthApiConfig {
    private String apiKey;
}
