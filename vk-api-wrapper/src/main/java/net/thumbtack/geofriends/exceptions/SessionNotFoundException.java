package net.thumbtack.geofriends.exceptions;

public class SessionNotFoundException extends BaseException {
    public SessionNotFoundException() {
        super(ErrorCode.SESSION_NOT_FOUND);
    }
}
