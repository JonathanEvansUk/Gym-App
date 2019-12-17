package com.evans.gymapp;

import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

import static com.evans.gymapp.service.impl.WorkoutDataService.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  // TODO replace with superclass exception?
  @ExceptionHandler({ResourceNotFoundException.class, WorkoutNotFoundException.class, ExerciseNotFoundException.class, ExerciseActivityNotFoundException.class})
  public ResponseEntity handleResourceNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity handleInvalidFormatException(InvalidFormatException exception) {
    return ResponseEntity.badRequest().body(exception);
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
