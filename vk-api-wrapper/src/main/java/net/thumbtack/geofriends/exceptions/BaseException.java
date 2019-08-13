package net.thumbtack.geofriends.exceptions;

public abstract class BaseException extends Exception {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public BaseException(String s, ErrorCode errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public BaseException(String s, Throwable throwable, ErrorCode errorCode) {
        super(s, throwable);
        this.errorCode = errorCode;
    }

    public BaseException(Throwable throwable, ErrorCode errorCode) {
        super(errorCode.getDescription(), throwable);
        this.errorCode = errorCode;
    }

    public BaseException(String s, Throwable throwable, boolean b, boolean b1, ErrorCode errorCode) {
        super(s, throwable, b, b1);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
