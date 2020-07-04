package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode(callSuper = true)
public class NonWeightedSet extends ExerciseSet {

  @NotBlank
  private final String weight;

  @Builder
  public NonWeightedSet(Long id, Integer numberOfReps, Status status, String weight) {
    super(id, numberOfReps, status);
    this.weight = weight;
  }
}
