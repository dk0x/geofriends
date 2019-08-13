package net.thumbtack.geofriends.vkapiwrapper.shared;

public enum ErrorCode {
    OAUTH_CODE_INVALID("Oauth code invalid or expired."),
    SESSION_NOT_FOUND("Session not found. Sign in again."),
    AUTHENTICATION_BROKEN("Authentication broken. Need sign in again.");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
