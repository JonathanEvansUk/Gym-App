package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class Exercise {

  private final Long id;
  private final String name;
  private final MuscleGroup muscleGroup;
  private final String information;
}
