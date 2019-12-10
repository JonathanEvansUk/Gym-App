package com.evans.gymapp.request;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class EditWorkoutRequest {

  @NonNull
  @NotBlank
  private final String workoutName;

  @NonNull
  private final WorkoutType workoutType;

  @NonNull
  private final Instant performedAtTimestampUtc;
}
