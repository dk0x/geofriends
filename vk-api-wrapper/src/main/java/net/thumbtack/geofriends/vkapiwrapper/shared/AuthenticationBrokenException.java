package net.thumbtack.geofriends.vkapiwrapper.shared;

public class AuthenticationBrokenException extends BaseException {
    public AuthenticationBrokenException(Throwable throwable) {
        super(throwable, ErrorCode.AUTHENTICATION_BROKEN);
    }
}
