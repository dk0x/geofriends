package net.thumbtack.geofriends;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.geofriends.vkapiwrapper.shared.ErrorDtoResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@RestControllerAdvice
@Order
@Slf4j
public class DefaultExceptionHandlingController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDtoResponse handleException(Exception ex) {
        String uniqueString = UUID.randomUUID().toString();
        log.error("Catched not properly handled exception (marker: '{}'). Exception: {} ", uniqueString, ex);
        return new ErrorDtoResponse("NOT_PROPERLY_HANDLED_EXCEPTION_CATCHED", "Log marker: " + uniqueString);
    }

}
