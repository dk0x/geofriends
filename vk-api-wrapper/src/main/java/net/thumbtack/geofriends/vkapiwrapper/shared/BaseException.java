package net.thumbtack.geofriends.vkapiwrapper.shared;

import lombok.Getter;

@Getter
public abstract class BaseException extends Exception {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public BaseException(Throwable throwable, ErrorCode errorCode) {
        super(errorCode.getDescription(), throwable);
        this.errorCode = errorCode;
    }

}
