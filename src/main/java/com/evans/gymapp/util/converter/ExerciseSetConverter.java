package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.ExerciseSet;
import com.evans.gymapp.persistence.entity.ExerciseSetEntity;
import org.springframework.stereotype.Component;

@Component
public class ExerciseSetConverter {

  public ExerciseSet convert(ExerciseSetEntity exerciseSetEntity) {
    return ExerciseSet.builder()
        .numberOfReps(exerciseSetEntity.getNumberOfReps())
        .weightKg(exerciseSetEntity.getWeightKg())
        .status(exerciseSetEntity.getStatus())
        .build();
  }

  public ExerciseSetEntity convert(ExerciseSet exerciseSet) {
    return ExerciseSetEntity.builder()
        .numberOfReps(exerciseSet.getNumberOfReps())
        .weightKg(exerciseSet.getWeightKg())
        .status(exerciseSet.getStatus())
        .build();
  }
}
