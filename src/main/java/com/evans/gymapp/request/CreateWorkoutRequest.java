package com.evans.gymapp.request;

import com.evans.gymapp.domain.WorkoutType;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
@Builder
public class CreateWorkoutRequest {

  @NotNull
  private final WorkoutType workoutType;

  @NotNull
  private final Instant performedAtTimestampUtc;
}
