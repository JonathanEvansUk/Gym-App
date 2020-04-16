package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.evans.gymapp.domain.MuscleGroup.BICEP;
import static com.evans.gymapp.domain.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class ExerciseActivityConverterTest {

  private ExerciseActivityConverter exerciseActivityConverter;

  @Mock
  private ExerciseConverter exerciseConverter;

  @Mock
  private ExerciseSetConverter exerciseSetConverter;

  private static final long EXERCISE_ID = 1L;
  private static final String EXERCISE_NAME = "Bicep Curl";
  private static final String EXERCISE_INFORMATION = "Bicep Curl with Dumbbell";

  private static final ExerciseEntity EXERCISE_ENTITY = ExerciseEntity.builder()
      .id(EXERCISE_ID)
      .name(EXERCISE_NAME)
      .information(EXERCISE_INFORMATION)
      .muscleGroup(BICEP)
      .build();

  private static final Exercise EXERCISE = Exercise.builder()
      .id(EXERCISE_ID)
      .name(EXERCISE_NAME)
      .information(EXERCISE_INFORMATION)
      .muscleGroup(BICEP)
      .build();

  private static final long EXERCISE_SET_ID = 1L;
  private static final double WEIGHT_KG = 5D;
  private static final int NUMBER_OF_REPS = 10;

  private static final ExerciseSetEntity EXERCISE_SET_ENTITY = WeightedSetEntity.builder()
      .id(EXERCISE_SET_ID)
      .weightKg(WEIGHT_KG)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  private static final ExerciseSet EXERCISE_SET = WeightedSet.builder()
      .id(EXERCISE_SET_ID)
      .weightKg(WEIGHT_KG)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  private static final long EXERCISE_ACTIVITY_ID = 1L;
  private static final String NOTES = "Some notes";

  private static final ExerciseActivity EXERCISE_ACTIVITY = ExerciseActivity.builder()
      .id(EXERCISE_ACTIVITY_ID)
      .exercise(EXERCISE)
      .sets(Collections.singletonList(EXERCISE_SET))
      .notes(NOTES)
      .build();

  private static final ExerciseActivityEntity EXERCISE_ACTIVITY_ENTITY = ExerciseActivityEntity.builder()
      .id(EXERCISE_ACTIVITY_ID)
      .exercise(EXERCISE_ENTITY)
      .sets(Collections.singletonList(EXERCISE_SET_ENTITY))
      .notes(NOTES)
      .build();

  @BeforeEach
  public void setUp() {
    exerciseActivityConverter = new ExerciseActivityConverter(exerciseConverter, exerciseSetConverter);
  }

  @Test
  public void convertFromEntity_noSets() {
    ExerciseActivityEntity exerciseActivityEntity = EXERCISE_ACTIVITY_ENTITY.toBuilder()
        .sets(Collections.emptyList())
        .build();

    ExerciseActivity expectedExerciseActivity = EXERCISE_ACTIVITY.toBuilder()
        .sets(Collections.emptyList())
        .build();

    given(exerciseConverter.convert(EXERCISE_ENTITY))
        .willReturn(EXERCISE);

    ExerciseActivity exerciseActivity = exerciseActivityConverter.convert(exerciseActivityEntity);

    assertEquals(expectedExerciseActivity, exerciseActivity);

    verify(exerciseConverter).convert(EXERCISE_ENTITY);
    verifyZeroInteractions(exerciseSetConverter);
  }

  @Test
  public void convertFromEntity() {
    given(exerciseConverter.convert(EXERCISE_ENTITY))
        .willReturn(EXERCISE);

    given(exerciseSetConverter.convert(EXERCISE_SET_ENTITY))
        .willReturn(EXERCISE_SET);

    ExerciseActivity exerciseActivity = exerciseActivityConverter.convert(EXERCISE_ACTIVITY_ENTITY);

    assertEquals(EXERCISE_ACTIVITY, exerciseActivity);

    verify(exerciseConverter).convert(EXERCISE_ENTITY);
    verify(exerciseSetConverter).convert(EXERCISE_SET_ENTITY);
  }

  @Test
  public void convertToEntity_noSets() {
    ExerciseActivity exerciseActivity = EXERCISE_ACTIVITY.toBuilder()
        .sets(Collections.emptyList())
        .build();

    ExerciseActivityEntity expectedExerciseActivityEntity = EXERCISE_ACTIVITY_ENTITY.toBuilder()
        .sets(Collections.emptyList())
        .build();

    given(exerciseConverter.convert(EXERCISE))
        .willReturn(EXERCISE_ENTITY);

    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);

    assertEquals(expectedExerciseActivityEntity, exerciseActivityEntity);

    verify(exerciseConverter).convert(EXERCISE);
    verifyZeroInteractions(exerciseSetConverter);
  }

  @Test
  public void convertToEntity() {
    given(exerciseConverter.convert(EXERCISE))
        .willReturn(EXERCISE_ENTITY);

    given(exerciseSetConverter.convert(EXERCISE_SET))
        .willReturn(EXERCISE_SET_ENTITY);

    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(EXERCISE_ACTIVITY);

    assertEquals(EXERCISE_ACTIVITY_ENTITY, exerciseActivityEntity);

    verify(exerciseConverter).convert(EXERCISE);
    verify(exerciseSetConverter).convert(EXERCISE_SET);
  }
}