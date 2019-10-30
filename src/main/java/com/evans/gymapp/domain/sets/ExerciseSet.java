package com.evans.gymapp.domain.sets;

import com.evans.gymapp.domain.Status;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @Type(value = WeightedSet.class),
    @Type(value = NonWeightedSet.class)
})
@Data
public abstract class ExerciseSet {

  private final Long id;
  private final Integer numberOfReps;
  private final Status status;
}
