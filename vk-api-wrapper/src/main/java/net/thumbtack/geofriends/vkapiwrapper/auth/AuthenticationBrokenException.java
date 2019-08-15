package net.thumbtack.geofriends.vkapiwrapper.auth;

import net.thumbtack.geofriends.vkapiwrapper.shared.BaseException;
import net.thumbtack.geofriends.vkapiwrapper.shared.ErrorCode;

public class AuthenticationBrokenException extends BaseException {
    public AuthenticationBrokenException(Throwable throwable) {
        super(throwable, ErrorCode.AUTHENTICATION_BROKEN);
    }
}
