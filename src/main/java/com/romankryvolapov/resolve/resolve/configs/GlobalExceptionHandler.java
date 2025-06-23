package com.romankryvolapov.resolve.resolve.configs;

import com.romankryvolapov.resolve.resolve.models.exceptions.AppException;
import com.romankryvolapov.resolve.resolve.models.exceptions.DependencyNotCompletedException;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(NotFoundServiceException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundServiceException ex) {
        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(DependencyNotCompletedException.class)
    public ResponseEntity<Map<String, String>> handleDependency(DependencyNotCompletedException ex) {
        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleApp(AppException ex) {
        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> error = Collections.singletonMap("error", "Internal server error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
