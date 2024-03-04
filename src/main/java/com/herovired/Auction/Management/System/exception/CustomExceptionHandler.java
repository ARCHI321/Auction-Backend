package com.herovired.Auction.Management.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleIdNotFoundException(Exception ex, WebRequest wb) {
        var duplicateUserExceptionResponse = new DuplicateUserExceptionResponse();
        duplicateUserExceptionResponse.setExceptionMessage(ex.getMessage());
        duplicateUserExceptionResponse.setExceptionCause(ex.getCause());
        duplicateUserExceptionResponse.setFileName(ex.getStackTrace()[0].getFileName());
        duplicateUserExceptionResponse.setMethodName(ex.getStackTrace()[0].getMethodName());
        return new ResponseEntity<>(duplicateUserExceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
