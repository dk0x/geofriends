package net.thumbtack.geofriends.vkapiwrapper.shared;

import com.vk.api.sdk.exceptions.ApiAuthException;
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
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ApiAuthException.class)
    public ErrorDtoResponse handleApiAuthException(ApiAuthException ex) throws ApiAuthException {
        if (ex.getCode().equals(5))
            return new ErrorDtoResponse(ErrorCode.VK_AUTH_EXPIRED.name(), "Vk auth expired. Need log in again.");
        throw ex;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SessionNotFoundException.class)
    public ErrorDtoResponse handleSessionNotFoundException(SessionNotFoundException ex) {
        return new ErrorDtoResponse(ErrorCode.SESSION_EXPIRED.name(), "Session expired. Need log in again.");
    }

    public enum ErrorCode {
        SESSION_EXPIRED, VK_AUTH_EXPIRED
    }
}
