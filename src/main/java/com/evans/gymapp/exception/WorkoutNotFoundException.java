package com.evans.gymapp.exception;

public class WorkoutNotFoundException extends ResourceNotFoundException {

  private static final String WORKOUT = "Workout";

  public WorkoutNotFoundException(long resourceId) {
    super(WORKOUT, resourceId);
  }
}
