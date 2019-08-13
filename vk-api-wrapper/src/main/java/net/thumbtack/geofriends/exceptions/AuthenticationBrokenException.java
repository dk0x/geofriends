package net.thumbtack.geofriends.exceptions;

public class AuthenticationBrokenException extends BaseException {
    public AuthenticationBrokenException() {
        super(ErrorCode.AUTHENTICATION_BROKEN);
    }

    public AuthenticationBrokenException(String s) {
        super(s, ErrorCode.AUTHENTICATION_BROKEN);
    }

    public AuthenticationBrokenException(String s, Throwable throwable) {
        super(s, throwable, ErrorCode.AUTHENTICATION_BROKEN);
    }

    public AuthenticationBrokenException(Throwable throwable) {
        super(throwable, ErrorCode.AUTHENTICATION_BROKEN);
    }

    public AuthenticationBrokenException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1, ErrorCode.AUTHENTICATION_BROKEN);
    }
}
