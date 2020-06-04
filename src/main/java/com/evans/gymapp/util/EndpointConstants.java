package com.evans.gymapp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointConstants {

  public static final String WORKOUTS = "/workouts";
  public static final String WORKOUT_ID = "/{workoutId}";

  public static final String WORKOUT_BY_ID = WORKOUTS + WORKOUT_ID;
  public static final String UPDATE_SETS = "/updateSets";
  public static final String ADD_EXERCISE_ACTIVITY = "/addExerciseActivity";
  public static final String DELETE_EXERCISE_ACTIVITY = "/exerciseActivity/{exerciseActivityId}";

  public static final String EXERCISES = "/exercises";
  public static final String EXERCISE_ID = "/{exerciseId}";

  public static final String WORKOUT_TYPES = "/workoutTypes";
}
