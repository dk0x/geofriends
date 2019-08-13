package net.thumbtack.geofriends.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthSession {
    private String sessionId;
    private String accessToken;
}
