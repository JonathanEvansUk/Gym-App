package com.evans.gymapp.exception;

public class ExerciseNotFoundException extends ResourceNotFoundException {

  private static final String EXERCISE = "Exercise";

  public ExerciseNotFoundException(long resourceId) {
    super(EXERCISE, resourceId);
  }
}
