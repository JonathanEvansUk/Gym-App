package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ExerciseActivity {

  private final List<ExerciseSet> sets;
  private final String notes;
}
