package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.service.IExerciseDataService;
import com.evans.gymapp.util.converter.ExerciseConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class ExerciseDataServiceTest {

  private IExerciseDataService exerciseDataService;

  @Mock
  private ExerciseRepository exerciseRepository;

  @Mock
  private WorkoutRepository workoutRepository;

  @Mock
  private ExerciseConverter exerciseConverter;

  @BeforeEach
  public void setUp() {
    exerciseDataService = new ExerciseDataService(exerciseRepository, workoutRepository, exerciseConverter);
  }

  @Test
  public void getAllExercises_noExercises() {

    given(exerciseRepository.findAll())
        .willReturn(Collections.emptyList());

    List<Exercise> allExercises = exerciseDataService.getAllExercises();

    assertEquals(Collections.emptyList(), allExercises);

    verify(exerciseRepository).findAll();
    verifyZeroInteractions(exerciseConverter);
  }

  @Test
  public void getAllExercises() {
    Exercise exercise = Exercise.builder()
        .id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Info")
        .build();

    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Information")
        .build();

    given(exerciseRepository.findAll())
        .willReturn(Collections.singletonList(exerciseEntity));

    given(exerciseConverter.convert(exerciseEntity))
        .willReturn(exercise);

    List<Exercise> allExercises = exerciseDataService.getAllExercises();

    assertEquals(Collections.singletonList(exercise), allExercises);

    verify(exerciseRepository).findAll();
    verify(exerciseConverter).convert(exerciseEntity);
  }

  @Test
  public void getExerciseById_exerciseNotFound() {

    long exerciseId = 1L;

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> exerciseDataService.getExerciseById(exerciseId));

    verify(exerciseRepository).findById(exerciseId);
    verifyZeroInteractions(exerciseConverter);
  }

  @Test
  public void getExerciseById() throws ExerciseNotFoundException {

    long exerciseId = 1L;

    Exercise exercise = Exercise.builder()
        .id(exerciseId)
        .muscleGroup(MuscleGroup.BICEP)
        .name("Bicep Curl")
        .information("Information")
        .build();

    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(exerciseId)
        .muscleGroup(MuscleGroup.BICEP)
        .name("Bicep Curl")
        .information("Information")
        .build();

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.of(exerciseEntity));

    given(exerciseConverter.convert(exerciseEntity))
        .willReturn(exercise);

    Exercise exerciseById = exerciseDataService.getExerciseById(exerciseId);

    assertEquals(exercise, exerciseById);

    verify(exerciseRepository).findById(exerciseId);
    verify(exerciseConverter).convert(exerciseEntity);
  }
}