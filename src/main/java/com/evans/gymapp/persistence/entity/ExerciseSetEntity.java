package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.Status;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ExerciseSetEntity {

  @NonNull
  private Integer numberOfReps;

  @NonNull
  private Double weightKg;

  @NonNull
  private Status status;

}
