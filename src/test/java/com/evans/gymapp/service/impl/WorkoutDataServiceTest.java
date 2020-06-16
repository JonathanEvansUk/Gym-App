package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.service.impl.WorkoutDataService.ResourceNotFoundException;
import com.evans.gymapp.util.converter.WorkoutConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutDataServiceTest {

  private IWorkoutDataService workoutDataService;

  @Mock
  private WorkoutRepository workoutRepository;

  @Mock
  private WorkoutConverter workoutConverter;

  @BeforeEach
  public void setUp() {
    workoutDataService = new WorkoutDataService(workoutRepository, workoutConverter);
  }

  @Test
  public void addWorkout() {
    Instant performedAtTimestampUtc = Instant.now();

    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(performedAtTimestampUtc)
        .build();

    WorkoutEntity workoutEntityFromRequest = WorkoutEntity.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(performedAtTimestampUtc)
        .exerciseActivities(Collections.emptyList())
        .build();

    given(workoutRepository.save(workoutEntityFromRequest))
        .willReturn(workoutEntityFromRequest);

    Workout expectedWorkout = Workout.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(performedAtTimestampUtc)
        .exerciseActivities(Collections.emptyList())
        .build();

    given(workoutConverter.convert(workoutEntityFromRequest))
        .willReturn(expectedWorkout);

    Workout workout = workoutDataService.addWorkout(request);

    verify(workoutRepository).save(workoutEntityFromRequest);
    verify(workoutConverter).convert(workoutEntityFromRequest);

    assertEquals(expectedWorkout, workout);
  }

  @Test
  public void editWorkout_workoutNotFound() {
    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(Instant.now())
        .build();

    long workoutId = 1L;

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> workoutDataService.editWorkout(workoutId, editWorkoutRequest));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(workoutConverter);
  }

  @Test
  public void editWorkout() throws WorkoutNotFoundException {
    Instant now = Instant.now();

    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    long workoutId = 1L;

    WorkoutEntity currentlyStoredWorkout = createWorkoutEntity();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(currentlyStoredWorkout));

    WorkoutEntity updatedWorkoutEntity = createWorkoutEntity().toBuilder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    given(workoutRepository.save(updatedWorkoutEntity))
        .willReturn(updatedWorkoutEntity);

    Workout expectedWorkout = Workout.builder()
        .id(workoutId)
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    given(workoutConverter.convert(updatedWorkoutEntity))
        .willReturn(expectedWorkout);

    Workout editedWorkout = workoutDataService.editWorkout(workoutId, editWorkoutRequest);


    assertEquals(expectedWorkout, editedWorkout);

    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).save(updatedWorkoutEntity);
    verify(workoutConverter).convert(updatedWorkoutEntity);
  }

  @Test
  public void deleteWorkout_workoutNotFound() {
    long workoutId = 1L;

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> workoutDataService.deleteWorkout(workoutId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
  }

  @Test
  public void deleteWorkout() throws WorkoutNotFoundException {
    long workoutId = 1L;

    WorkoutEntity workoutEntity = createWorkoutEntity();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    workoutDataService.deleteWorkout(workoutId);

    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).delete(workoutEntity);
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
    WorkoutEntity workoutEntity = createWorkoutEntity();

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

    assertThrows(ResourceNotFoundException.class, () -> workoutDataService.getWorkoutById(1L));

    verify(workoutRepository).findById(1L);
    verifyZeroInteractions(workoutConverter);
  }

  @Test
  public void getWorkoutById() {
    WorkoutEntity workoutEntity = createWorkoutEntity();

    given(workoutRepository.findById(1L))
        .willReturn(Optional.of(workoutEntity));

    Workout expectedWorkout = Workout.builder().build();

    given(workoutConverter.convert(workoutEntity))
        .willReturn(expectedWorkout);

    Workout workout = workoutDataService.getWorkoutById(1L);

    verify(workoutRepository).findById(1L);
    verify(workoutConverter).convert(workoutEntity);
    assertEquals(expectedWorkout, workout);
  }

  private WorkoutEntity createWorkoutEntity() {
    return WorkoutEntity.builder()
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();
  }
}