package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import com.evans.gymapp.util.converter.ExerciseSetConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class ExerciseActivityDataServiceTest {

  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  private IExerciseActivityDataService exerciseActivityDataService;

  @Mock
  private ExerciseActivityRepository exerciseActivityRepository;

  @Mock
  private ExerciseActivityConverter exerciseActivityConverter;

  @Mock
  private ExerciseSetConverter exerciseSetConverter;

  @BeforeEach
  public void setUp() {
    exerciseActivityDataService = new ExerciseActivityDataService(exerciseActivityRepository, exerciseSetConverter);
  }

  @Test
  public void updateSets_exerciseActivityNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId);

    given(exerciseActivityRepository.findById(exerciseActivityId))
        .willReturn(Optional.empty());

    assertThrows(ExerciseActivityNotFoundException.class,
        () -> exerciseActivityDataService.updateSets(workoutId, exerciseActivity));
  }

  @Test
  public void updateSets_noSets() throws ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId);

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);

    given(exerciseActivityRepository.findById(exerciseActivityId))
        .willReturn(Optional.of(exerciseActivityEntity));

    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);

    verify(exerciseActivityRepository).findById(exerciseActivityId);
    verifyZeroInteractions(exerciseActivityConverter);
    verify(exerciseActivityRepository).save(exerciseActivityEntity);
  }

  @Test
  public void updateSets() throws ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseSet weightedSet = createWeightedSet();

    ExerciseActivity newExerciseActivity = createExerciseActivity(exerciseActivityId)
        .toBuilder()
        .sets(Collections.singletonList(weightedSet))
        .build();

    ExerciseSetEntity weightedSetEntity = createWeightedSetEntityBuilder().build();

    ExerciseActivityEntity existingExerciseActivity = createExerciseActivityEntity(exerciseActivityId)
        .toBuilder()
        .sets(Collections.emptyList())
        .build();

    given(exerciseActivityRepository.findById(exerciseActivityId))
        .willReturn(Optional.of(existingExerciseActivity));

    given(exerciseSetConverter.convert(weightedSet))
        .willReturn(weightedSetEntity);

    exerciseActivityDataService.updateSets(workoutId, newExerciseActivity);

    ExerciseActivityEntity updatedExerciseActivityEntity = existingExerciseActivity.toBuilder()
        .sets(new ArrayList<>())
        .build();

    ExerciseSetEntity updatedWeightedSetEntity = createWeightedSetEntityBuilder()
        .exerciseActivityEntity(updatedExerciseActivityEntity)
        .build();

    updatedExerciseActivityEntity.getSets().add(updatedWeightedSetEntity);

    verify(exerciseActivityRepository).findById(exerciseActivityId);
    verify(exerciseSetConverter).convert(weightedSet);
    verify(exerciseActivityRepository).save(updatedExerciseActivityEntity);
    verify(exerciseActivityRepository).save(argThat(
        arg -> arg.getSets()
            .stream()
            .map(ExerciseSetEntity::getExerciseActivity)
            .map(ExerciseActivityEntity::getId)
            .allMatch(id -> id.equals(exerciseActivityId))
    ));
  }

  private WeightedSetEntity.WeightedSetEntityBuilder createWeightedSetEntityBuilder() {
    return WeightedSetEntity.builder()
        .weightKg(1D)
        .numberOfReps(4)
        .status(Status.FAILED);
  }

  private WeightedSet createWeightedSet() {
    return WeightedSet.builder()
        .weightKg(1D)
        .numberOfReps(4)
        .status(Status.FAILED)
        .build();
  }

  private ExerciseEntity createExerciseEntity(long exerciseId) {
    return ExerciseEntity.builder()
        .id(exerciseId)
        .name(TRICEP_PUSHDOWN)
        .muscleGroup(MuscleGroup.TRICEP)
        .build();
  }

  private Exercise createExercise() {
    return Exercise.builder()
        .name(TRICEP_PUSHDOWN)
        .muscleGroup(MuscleGroup.TRICEP)
        .build();
  }

  private ExerciseActivity createExerciseActivity(long exerciseActivityId) {
    return ExerciseActivity.builder()
        .id(exerciseActivityId)
        .exercise(createExercise())
        .sets(Collections.emptyList())
        .build();
  }

  private ExerciseActivityEntity createExerciseActivityEntity(long exerciseActivityEntityId) {
    ExerciseEntity exerciseEntity = createExerciseEntity(1L);

    return ExerciseActivityEntity.builder()
        .id(exerciseActivityEntityId)
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();
  }
}