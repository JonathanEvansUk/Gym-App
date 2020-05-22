package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class NonWeightedSet extends ExerciseSet {

  private final String weight;

  @Builder
  public NonWeightedSet(Long id, int numberOfReps, Status status, String weight) {
    super(id, numberOfReps, status);
    this.weight = weight;
  }
}
