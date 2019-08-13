package net.thumbtack.geofriends.exceptions;

public class AuthenticationBrokenException extends BaseException {
    public AuthenticationBrokenException(Throwable throwable) {
        super(throwable, ErrorCode.AUTHENTICATION_BROKEN);
    }
}
