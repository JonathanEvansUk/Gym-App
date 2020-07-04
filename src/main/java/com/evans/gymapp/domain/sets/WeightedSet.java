package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode(callSuper = true)
public class WeightedSet extends ExerciseSet {

  @NotNull
  private final Double weightKg;

  @Builder
  public WeightedSet(Long id, Integer numberOfReps, Status status, Double weightKg) {
    super(id, numberOfReps, status);
    this.weightKg = weightKg;
  }
}
