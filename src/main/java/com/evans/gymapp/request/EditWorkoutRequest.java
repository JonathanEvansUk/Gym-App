package com.evans.gymapp.request;

import com.evans.gymapp.domain.WorkoutType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class EditWorkoutRequest {

  @NonNull
  private final WorkoutType workoutType;

  @NonNull
  private final Instant performedAtTimestampUtc;
}
