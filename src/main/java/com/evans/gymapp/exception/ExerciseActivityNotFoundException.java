package com.evans.gymapp.exception;

public class ExerciseActivityNotFoundException extends ResourceNotFoundException {

  private static final String EXERCISE_ACTIVITY = "Exercise Activity";

  public ExerciseActivityNotFoundException(long resourceId) {
    super(EXERCISE_ACTIVITY, resourceId);
  }
}
