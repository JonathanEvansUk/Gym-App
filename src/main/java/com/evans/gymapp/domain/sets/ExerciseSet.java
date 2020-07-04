package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @Type(value = WeightedSet.class),
    @Type(value = NonWeightedSet.class)
})
@Data
public abstract class ExerciseSet {

  private final Long id;

  @NotNull
  private final Integer numberOfReps;

  @NotNull
  private final Status status;
}
