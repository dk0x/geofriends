package net.thumbtack.geofriends.vkapiwrapper.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseException.class)
    public ErrorDtoResponse handleBaseException(BaseException ex) {
        return new ErrorDtoResponse(ex.getErrorCode().name(), ex.getMessage());
    }

}
