package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseConverterTest {

  private final ExerciseConverter exerciseConverter = new ExerciseConverter();

  @Test
  public void convertFromEntity() {
    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    Exercise exercise = exerciseConverter.convert(exerciseEntity);

    assertEquals(exerciseEntity.getId(), exercise.getId());
    assertEquals(exerciseEntity.getName(), exercise.getName());
    assertEquals(exerciseEntity.getInformation(), exercise.getInformation());
    assertEquals(exerciseEntity.getMuscleGroup(), exercise.getMuscleGroup());
  }

  @Test
  public void convertToEntity() {
    Exercise exercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseEntity exerciseEntity = exerciseConverter.convert(exercise);

    assertEquals(exercise.getId(), exerciseEntity.getId());
    assertEquals(exercise.getName(), exerciseEntity.getName());
    assertEquals(exercise.getInformation(), exerciseEntity.getInformation());
    assertEquals(exercise.getMuscleGroup(), exerciseEntity.getMuscleGroup());
  }
}