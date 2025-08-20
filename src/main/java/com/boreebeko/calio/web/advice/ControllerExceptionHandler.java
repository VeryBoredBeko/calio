package com.boreebeko.calio.web.advice;

import com.boreebeko.calio.exception.InvalidEventTimingeException;
import com.boreebeko.calio.exception.NoSuchEventEntityException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> violations = new HashMap<>(exception.getErrorCount());
        exception.getBindingResult().getFieldErrors().forEach(violation ->
                violations.put(violation.getField(), violation.getDefaultMessage()));
        return new ResponseEntity<>(violations, HttpStatus.BAD_REQUEST);
    }
}
