package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.util.converter.WorkoutConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutDataServiceTest {

  private WorkoutDataService workoutDataService;

  @Mock
  private WorkoutRepository workoutRepository;

  @Mock
  private ExerciseRepository exerciseRepository;

  @Mock
  private WorkoutConverter workoutConverter;

  @Before
  public void setUp() {
    workoutDataService = new WorkoutDataService(workoutRepository, exerciseRepository, workoutConverter);
  }

  @Test
  public void addWorkout() {
    fail();
  }

  @Test
  public void getAllWorkouts_noStoredWorkouts() {
    given(workoutRepository.findAll())
        .willReturn(Collections.emptyList());

    List<Workout> workouts = workoutDataService.getAllWorkouts();

    verify(workoutRepository).findAll();
    verifyZeroInteractions(workoutConverter);
    assertEquals(Collections.emptyList(), workouts);
  }

  @Test
  public void getAllWorkouts() {
    WorkoutEntity workoutEntity = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptySet())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    given(workoutRepository.findAll())
        .willReturn(Collections.singletonList(workoutEntity));

    given(workoutConverter.convert(workoutEntity))
        .willReturn(Workout.builder().build());

    List<Workout> workouts = workoutDataService.getAllWorkouts();

    verify(workoutRepository).findAll();
    verify(workoutConverter).convert(workoutEntity);
    assertEquals(1, workouts.size());
  }

  @Test
  public void getWorkoutById_noWorkout() {
    given(workoutRepository.findById(1L))
        .willReturn(Optional.empty());

    Optional<Workout> workout = workoutDataService.getWorkoutById(1L);

    verify(workoutRepository).findById(1L);
    verifyZeroInteractions(workoutConverter);
    assertFalse(workout.isPresent());
  }

  @Test
  public void getWorkoutById() {
    WorkoutEntity workoutEntity = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptySet())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    given(workoutRepository.findById(1L))
        .willReturn(Optional.of(workoutEntity));

    given(workoutConverter.convert(workoutEntity))
        .willReturn(Workout.builder().build());

    Optional<Workout> workout = workoutDataService.getWorkoutById(1L);

    verify(workoutRepository).findById(1L);
    verify(workoutConverter).convert(workoutEntity);
    assertTrue(workout.isPresent());
  }

  @Test
  public void getWorkoutByName_noWorkout() {
    given(workoutRepository.findByName("workoutName"))
        .willReturn(Optional.empty());

    Optional<Workout> workout = workoutDataService.getWorkoutByName("workoutName");

    verify(workoutRepository).findByName("workoutName");
    verifyZeroInteractions(workoutConverter);
    assertFalse(workout.isPresent());
  }

  @Test
  public void getWorkoutByName() {
    WorkoutEntity workoutEntity = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptySet())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    given(workoutRepository.findByName("workoutName"))
        .willReturn(Optional.of(workoutEntity));

    given(workoutConverter.convert(workoutEntity))
        .willReturn(Workout.builder().build());

    Optional<Workout> workout = workoutDataService.getWorkoutByName("workoutName");

    verify(workoutRepository).findByName("workoutName");
    verify(workoutConverter).convert(workoutEntity);
    assertTrue(workout.isPresent());
  }
}