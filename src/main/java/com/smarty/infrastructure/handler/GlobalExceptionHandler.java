package com.smarty.infrastructure.handler;

import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<ApiErrorResponse> handleConflictException(Exception e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorResponse response = new ApiErrorResponse(e.getMessage(), status, ZonedDateTime.now());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse response = new ApiErrorResponse(e.getMessage(), status, ZonedDateTime.now());

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler
    public Map<String, String> handleMethodArgumentException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }

}
