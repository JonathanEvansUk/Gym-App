package com.evans.gymapp.request;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class EditWorkoutRequest {

  @NonNull
  private final WorkoutType workoutType;

  @NonNull
  private final Instant performedAtTimestampUtc;
}
