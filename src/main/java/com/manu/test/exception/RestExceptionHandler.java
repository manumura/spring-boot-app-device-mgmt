package com.manu.test.exception;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

import com.manu.test.util.ValidationUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * @author emmanuel.mura
 *
 */
// TODO : not used (test)
//@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        String message = "You are not allowed to access this page";
        return new ResponseEntity<>(Arrays.asList(message), new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return new ResponseEntity<>(new CustomError(ValidationUtil.fromBindingErrors(result)),
                HttpStatus.BAD_REQUEST);
    }
}
