package net.thumbtack.geofriends.exceptions;

public class OauthCodeInvalidException extends BaseException {
    public OauthCodeInvalidException() {
        super(ErrorCode.OAUTH_CODE_INVALID);
    }

    public OauthCodeInvalidException(String s) {
        super(s, ErrorCode.OAUTH_CODE_INVALID);
    }

    public OauthCodeInvalidException(String s, Throwable throwable) {
        super(s, throwable, ErrorCode.OAUTH_CODE_INVALID);
    }

    public OauthCodeInvalidException(Throwable throwable) {
        super(throwable, ErrorCode.OAUTH_CODE_INVALID);
    }

    public OauthCodeInvalidException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1, ErrorCode.OAUTH_CODE_INVALID);
    }
}
