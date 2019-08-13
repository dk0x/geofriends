package net.thumbtack.geofriends.model;

import java.util.Objects;

public class AuthSession {
    private String sessionId;
    private String accessToken;

    public AuthSession(String sessionId, String accessToken) {
        this.sessionId = sessionId;
        this.accessToken = accessToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VkAuthSession{");
        sb.append("sessionId='").append(sessionId).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AuthSession))
            return false;
        AuthSession that = (AuthSession) o;
        return Objects.equals(getSessionId(), that.getSessionId()) &&
                Objects.equals(getAccessToken(), that.getAccessToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId(), getAccessToken());
    }
}
