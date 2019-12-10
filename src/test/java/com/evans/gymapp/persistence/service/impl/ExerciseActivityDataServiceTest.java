package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExerciseActivityDataServiceTest {

  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  private IExerciseActivityDataService exerciseActivityDataService;

  @Mock
  private ExerciseActivityRepository exerciseActivityRepository;

  @Mock
  private ExerciseActivityConverter exerciseActivityConverter;

  @BeforeEach
  public void setUp() {
    exerciseActivityDataService = new ExerciseActivityDataService(exerciseActivityRepository, exerciseActivityConverter);
  }

  @Test
  public void updateSets() {
    Exercise exercise = createExercise();

    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .id(exerciseActivityId)
        .exercise(exercise)
        .sets(Collections.emptyList())
        .build();

    long workoutId = 1L;

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);

    given(exerciseActivityConverter.convert(exerciseActivity))
        .willReturn(exerciseActivityEntity);

    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);

    verify(exerciseActivityConverter).convert(exerciseActivity);
    verify(exerciseActivityRepository).save(exerciseActivityEntity);
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

  private ExerciseActivityEntity createExerciseActivityEntity(long exerciseActivityEntityId) {
    ExerciseEntity exerciseEntity = createExerciseEntity(1L);

    return ExerciseActivityEntity.builder()
        .id(exerciseActivityEntityId)
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();
  }

}