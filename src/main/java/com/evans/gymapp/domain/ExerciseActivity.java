package com.evans.gymapp.domain;

import com.evans.gymapp.domain.sets.ExerciseSet;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ExerciseActivity {

  private final Long id;
  private final Exercise exercise;

  @Valid
  @NotNull
  private final List<ExerciseSet> sets;
  private final String notes;
}
