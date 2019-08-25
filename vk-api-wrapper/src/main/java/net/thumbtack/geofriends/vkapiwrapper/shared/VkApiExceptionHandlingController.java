package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.vk.api.sdk.exceptions.ApiAuthException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class VkApiExceptionHandlingController {
    protected final static String DESCRIPTION_API_AUTH_EXCEPTION = "Vk auth expired. Need log in again.";
    protected final static String DESCRIPTION_SESSION_EXPIRED_EXCEPTION = "Session expired. Need log in again.";

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ApiAuthException.class)
    public ErrorDtoResponse handleApiAuthException(ApiAuthException ex) throws ApiAuthException {
        if (ex.getCode().equals(5)) {
            return new ErrorDtoResponse(ErrorCode.VK_AUTH_EXPIRED, DESCRIPTION_API_AUTH_EXCEPTION);
        } else {
            throw ex;
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SessionExpiredException.class)
    public ErrorDtoResponse handleSessionExpiredException(SessionExpiredException ex) {
        return new ErrorDtoResponse(ErrorCode.SESSION_EXPIRED, DESCRIPTION_SESSION_EXPIRED_EXCEPTION);
    }

    protected enum ErrorCode {
        SESSION_EXPIRED, VK_AUTH_EXPIRED
    }

    @Data
    protected static class ErrorDtoResponse {
        private final ErrorCode errorCode;
        private final String description;
    }
}
