package net.thumbtack.geofriends.vkapiwrapper.auth;

import net.thumbtack.geofriends.vkapiwrapper.shared.BaseException;
import net.thumbtack.geofriends.vkapiwrapper.shared.ErrorCode;

public class AuthCodeInvalidException extends BaseException {
    public AuthCodeInvalidException(Throwable throwable) {
        super(throwable, ErrorCode.OAUTH_CODE_INVALID);
    }
}
