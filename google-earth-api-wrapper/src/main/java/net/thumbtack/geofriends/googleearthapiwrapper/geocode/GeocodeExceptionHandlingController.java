package net.thumbtack.geofriends.googleearthapiwrapper.geocode;


import com.google.maps.errors.ApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GeocodeExceptionHandlingController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApiException.class)
    public ErrorDtoResponse handleApiException(ApiException ex) {
        String errorTicket = UUID.randomUUID().toString();
        log.error("Catched google api exception (ticket: '{}'). Exception: {} ", errorTicket, ex);
        return new ErrorDtoResponse(ErrorCode.GOOGLE_API_ERROR, "Something wrong with google geocode api. Please send to tech support this ticket: " + errorTicket);
    }

    protected enum ErrorCode {
        GOOGLE_API_ERROR,
    }

    @Data
    protected static class ErrorDtoResponse {
        private final ErrorCode errorCode;
        private final String description;
    }

}
