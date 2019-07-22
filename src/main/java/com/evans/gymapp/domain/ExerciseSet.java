package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ExerciseSet {

  private final int numberOfReps;
  private final double weightKg;
  private final Status status;
}
