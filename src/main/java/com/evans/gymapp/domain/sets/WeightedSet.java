package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import lombok.Builder;
import lombok.Getter;

//@Data
@Getter
public class WeightedSet extends ExerciseSet {

  private final Double weightKg;

  @Builder
  public WeightedSet(Long id, Integer numberOfReps, Status status, Double weightKg) {
    super(id, numberOfReps, status);
    this.weightKg = weightKg;
  }
}
