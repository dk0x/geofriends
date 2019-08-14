package net.thumbtack.geofriends.vkapiwrapper.auth;

import net.thumbtack.geofriends.vkapiwrapper.shared.BaseException;
import net.thumbtack.geofriends.vkapiwrapper.shared.ErrorCode;

public class SessionNotFoundException extends BaseException {
    public SessionNotFoundException() {
        super(ErrorCode.SESSION_NOT_FOUND);
    }
}
