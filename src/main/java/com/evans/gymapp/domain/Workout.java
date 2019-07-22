package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@RequiredArgsConstructor
public class Workout {

  private final String name;
  private final WorkoutType workoutType;
  private final Map<Exercise, ExerciseActivity> exerciseActivity;
  private final Instant performedAtTimestampUtc;
  private final String notes;
}
