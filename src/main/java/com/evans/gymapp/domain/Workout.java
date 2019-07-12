package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class Workout {

  private final String name;
  private final WorkoutType workoutType;
  private final List<Exercise> exercises;
}
