package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@Data
public abstract class ExerciseSet {

  private final Long id;
  private final Integer numberOfReps;
  private final Status status;
}
