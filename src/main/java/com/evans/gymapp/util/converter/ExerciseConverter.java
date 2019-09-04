package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExerciseConverter {

  public Exercise convert(ExerciseEntity exerciseEntity) {
    return Exercise.builder()
        .id(exerciseEntity.getId())
        .name(exerciseEntity.getName())
        .muscleGroup(exerciseEntity.getMuscleGroup())
        .information(exerciseEntity.getInformation())
        .build();
  }

  public ExerciseEntity convert(Exercise exercise) {
    return ExerciseEntity.builder()
        .id(exercise.getId())
        .name(exercise.getName())
        .muscleGroup(exercise.getMuscleGroup())
        .information(exercise.getInformation())
        .build();
  }
}
