package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import org.junit.jupiter.api.Test;

import static com.evans.gymapp.domain.MuscleGroup.BICEP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExerciseConverterTest {

  private final ExerciseConverter exerciseConverter = new ExerciseConverter();

  private static final long ID = 1L;
  private static final String NAME = "Bicep Curl";
  private static final String INFORMATION = "Information";

  private static final ExerciseEntity EXERCISE_ENTITY = ExerciseEntity.builder()
      .id(ID)
      .name(NAME)
      .information(INFORMATION)
      .muscleGroup(BICEP)
      .build();

  private static final Exercise EXERCISE = Exercise.builder()
      .id(ID)
      .name(NAME)
      .information(INFORMATION)
      .muscleGroup(BICEP)
      .build();

  @Test
  public void convertFromEntity() {
    Exercise exercise = exerciseConverter.convert(EXERCISE_ENTITY);

    assertEquals(EXERCISE, exercise);
  }

  @Test
  public void convertToEntity() {
    ExerciseEntity exerciseEntity = exerciseConverter.convert(EXERCISE);

    assertEquals(EXERCISE_ENTITY, exerciseEntity);
  }
}