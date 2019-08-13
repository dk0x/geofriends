package net.thumbtack.geofriends.exceptions;

public class OauthCodeInvalidException extends BaseException {
    public OauthCodeInvalidException(Throwable throwable) {
        super(throwable, ErrorCode.OAUTH_CODE_INVALID);
    }
}
