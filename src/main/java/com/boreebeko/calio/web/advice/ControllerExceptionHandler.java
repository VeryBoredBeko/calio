package com.boreebeko.calio.web.advice;

import com.boreebeko.calio.exception.InvalidEventTimingeException;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoSuchEventEntityException.class)
    public ResponseEntity<String> handleNoSuchEventEntityException(NoSuchEventEntityException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventTimingeException.class)
    public ResponseEntity<String> handleInvalidEventTiming(InvalidEventTimingeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
