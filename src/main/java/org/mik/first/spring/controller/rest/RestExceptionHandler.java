package org.mik.first.spring.controller.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class RestExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFound(ResourceNotFoundException e, WebRequest request){
        log.warn("Resource Not Found",String.format("error:%s,Request:%s",e.getMessage(),request));
        return ErrorMessage.builder()
                .message("Not Found")
                .statusCode(HttpStatus.NO_CONTENT.value())
                .description(e.getMessage())
                .build();
    }
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage resourceAlreadyExist(ResourceAlreadyExistException e, WebRequest request){
        log.warn("Resource Already Exist",String.format("error:%s,Request:%s",e.getMessage(),request));
        return ErrorMessage.builder()
                .message("Not Found")
                .statusCode(HttpStatus.CONTINUE.value())
                .description(e.getMessage())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleServerError(Exception e,WebRequest request){
        log.error("Server Error",e);
return ErrorMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .description("see log")
                                .message("server error").build();
    }
}
