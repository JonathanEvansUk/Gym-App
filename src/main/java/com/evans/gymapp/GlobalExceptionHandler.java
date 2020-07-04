package com.evans.gymapp;

import com.evans.gymapp.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity handleResourceNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    Map<String, String> errors = exception.getBindingResult().getAllErrors().stream()
        .filter(FieldError.class::isInstance)
        .map(FieldError.class::cast)
        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

    return ResponseEntity.badRequest().body(errors);
  }
}
