package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class Workout {

  private final Long id;
  private final String name;
  private final WorkoutType workoutType;
  private final List<ExerciseActivity> exerciseActivities;
  private final Instant performedAtTimestampUtc;
  private final String notes;
}
