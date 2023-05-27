package com.smarty.infrastructure.handler;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiErrorResponse(

        String message,
        HttpStatus status,
        ZonedDateTime timestamp

) {

}
