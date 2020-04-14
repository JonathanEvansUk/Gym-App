package com.evans.gymapp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointConstants {

  public static final String WORKOUTS = "/workouts";
  public static final String WORKOUT_ID = "/{workoutId}";

  public static final String EXERCISES = "/exercises";
  public static final String EXERCISE_ID = "/{exerciseId}";

  public static final String WORKOUT_TYPES = "/workoutTypes";
}
