package com.evans.gymapp.controller;

import com.evans.gymapp.domain.WorkoutType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class CreateWorkoutRequest {

  @NonNull
  @NotBlank
  private final String workoutName;

  @NonNull
  private final WorkoutType workoutType;

  @NonNull
  private final Instant performedAtTimestampUtc;
}
