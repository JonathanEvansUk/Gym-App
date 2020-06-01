package com.evans.gymapp.domain;

import com.evans.gymapp.domain.sets.ExerciseSet;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ExerciseActivity {

  private final Long id;
  private final Exercise exercise;

  @NotNull
  private final List<ExerciseSet> sets;
  private final String notes;
}
