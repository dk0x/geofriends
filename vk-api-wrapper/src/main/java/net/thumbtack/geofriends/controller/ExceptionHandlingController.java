package net.thumbtack.geofriends.controller;

import net.thumbtack.geofriends.dto.response.ErrorDtoResponse;
import net.thumbtack.geofriends.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseException.class)
    public ErrorDtoResponse handleVkApiBaseException(BaseException ex) {
        return new ErrorDtoResponse(ex.getErrorCode().name(), ex.getMessage());
    }

}
