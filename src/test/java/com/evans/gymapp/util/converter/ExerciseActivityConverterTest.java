package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
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
import java.util.List;

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

  @BeforeEach
  public void setUp() {
    exerciseActivityConverter = new ExerciseActivityConverter(exerciseConverter, exerciseSetConverter);
  }

  @Test
  public void convertFromEntity_noSets() {
    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .id(1L)
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .notes("Some notes")
        .build();

    Exercise expectedExercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    given(exerciseConverter.convert(exerciseEntity))
        .willReturn(expectedExercise);

    ExerciseActivity exerciseActivity = exerciseActivityConverter.convert(exerciseActivityEntity);

    assertEquals(exerciseActivityEntity.getId(), exerciseActivity.getId());
    assertEquals(expectedExercise, exerciseActivity.getExercise());
    assertEquals(Collections.emptyList(), exerciseActivity.getSets());
    assertEquals(exerciseActivityEntity.getNotes(), exerciseActivity.getNotes());

    verify(exerciseConverter).convert(exerciseEntity);
    verifyZeroInteractions(exerciseSetConverter);
  }

  @Test
  public void convertFromEntity() {
    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseSetEntity exerciseSetEntity = WeightedSetEntity.builder()
        .id(1L)
        .weightKg(5D)
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();

    List<ExerciseSetEntity> exerciseSetEntities = Collections.singletonList(exerciseSetEntity);

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .id(1L)
        .exercise(exerciseEntity)
        .sets(exerciseSetEntities)
        .notes("Some notes")
        .build();

    Exercise expectedExercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    given(exerciseConverter.convert(exerciseEntity))
        .willReturn(expectedExercise);

    ExerciseSet expectedExerciseSet = WeightedSet.builder()
        .id(1L)
        .weightKg(5D)
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();

    given(exerciseSetConverter.convert(exerciseSetEntity))
        .willReturn(expectedExerciseSet);

    ExerciseActivity exerciseActivity = exerciseActivityConverter.convert(exerciseActivityEntity);

    assertEquals(exerciseActivityEntity.getId(), exerciseActivity.getId());
    assertEquals(expectedExercise, exerciseActivity.getExercise());
    assertEquals(Collections.singletonList(expectedExerciseSet), exerciseActivity.getSets());
    assertEquals(exerciseActivityEntity.getNotes(), exerciseActivity.getNotes());

    verify(exerciseConverter).convert(exerciseEntity);
    verify(exerciseSetConverter).convert(exerciseSetEntity);
  }

  @Test
  public void convertToEntity_noSets() {
    Exercise exercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .id(1L)
        .exercise(exercise)
        .sets(Collections.emptyList())
        .notes("Some notes")
        .build();

    ExerciseEntity expectedExercise = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    given(exerciseConverter.convert(exercise))
        .willReturn(expectedExercise);

    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);

    assertEquals(exerciseActivity.getId(), exerciseActivityEntity.getId());
    assertEquals(expectedExercise, exerciseActivityEntity.getExercise());
    assertEquals(Collections.emptyList(), exerciseActivityEntity.getSets());
    assertEquals(exerciseActivity.getNotes(), exerciseActivityEntity.getNotes());

    verify(exerciseConverter).convert(exercise);
    verifyZeroInteractions(exerciseSetConverter);
  }

  @Test
  public void convertToEntity() {
    Exercise exercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseSet exerciseSet = WeightedSet.builder()
        .id(1L)
        .weightKg(5D)
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .id(1L)
        .exercise(exercise)
        .sets(Collections.singletonList(exerciseSet))
        .notes("Some notes")
        .build();

    ExerciseEntity expectedExercise = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .information("Bicep Curl with Dumbbell")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    given(exerciseConverter.convert(exercise))
        .willReturn(expectedExercise);

    ExerciseSetEntity expectedExerciseSet = WeightedSetEntity.builder()
        .id(1L)
        .weightKg(5D)
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();

    given(exerciseSetConverter.convert(exerciseSet))
        .willReturn(expectedExerciseSet);

    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);

    assertEquals(exerciseActivity.getId(), exerciseActivityEntity.getId());
    assertEquals(expectedExercise, exerciseActivityEntity.getExercise());
    assertEquals(Collections.singletonList(expectedExerciseSet), exerciseActivityEntity.getSets());
    assertEquals(exerciseActivity.getNotes(), exerciseActivityEntity.getNotes());

    verify(exerciseConverter).convert(exercise);
    verify(exerciseSetConverter).convert(exerciseSet);
  }
}