package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.table.ExerciseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExerciseConverter {

  public Exercise convert(ExerciseEntity exerciseEntity) {
    return Exercise.builder()
        .name(exerciseEntity.getName())
        .muscleGroup(exerciseEntity.getMuscleGroup())
        .information(exerciseEntity.getInformation())
        .build();
  }

  public ExerciseEntity convert(Exercise exercise) {
    return ExerciseEntity.builder()
        .name(exercise.getName())
        .muscleGroup(exercise.getMuscleGroup())
        .information(exercise.getInformation())
        .build();
  }
}
