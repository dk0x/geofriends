package net.thumbtack.geofriends.exceptions;

public class SessionNotFoundException extends BaseException {
    public SessionNotFoundException() {
        super(ErrorCode.SESSION_NOT_FOUND);
    }

    public SessionNotFoundException(String s) {
        super(s, ErrorCode.SESSION_NOT_FOUND);
    }

    public SessionNotFoundException(String s, Throwable throwable) {
        super(s, throwable, ErrorCode.SESSION_NOT_FOUND);
    }

    public SessionNotFoundException(Throwable throwable) {
        super(throwable, ErrorCode.SESSION_NOT_FOUND);
    }

    public SessionNotFoundException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1, ErrorCode.SESSION_NOT_FOUND);
    }
}
