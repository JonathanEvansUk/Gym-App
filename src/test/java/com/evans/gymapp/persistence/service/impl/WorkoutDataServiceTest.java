package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.controller.CreateWorkoutRequest;
import com.evans.gymapp.controller.ExerciseActivityNotFoundException;
import com.evans.gymapp.controller.ExerciseNotFoundException;
import com.evans.gymapp.controller.WorkoutNotFoundException;
import com.evans.gymapp.domain.*;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutDataServiceTest {

  private static final String WORKOUT_NAME = "workoutName";
  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  private WorkoutDataService workoutDataService;

  @Mock
  private WorkoutRepository workoutRepository;

  @Mock
  private ExerciseRepository exerciseRepository;

  @Mock
  private WorkoutConverter workoutConverter;

  @Mock
  private ExerciseActivityConverter exerciseActivityConverter;

  @Mock
  private ExerciseActivityRepository exerciseActivityRepository;

  @BeforeEach
  public void setUp() {
    workoutDataService = new WorkoutDataService(workoutRepository, exerciseRepository, exerciseActivityRepository, workoutConverter, exerciseActivityConverter);
  }

  @Test
  public void addWorkout() {
    String workoutName = "workoutName";
    Instant performedAtTimestampUtc = Instant.now();

    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutName(workoutName)
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(performedAtTimestampUtc)
        .build();

    WorkoutEntity workoutEntityFromRequest = WorkoutEntity.builder()
        .name(workoutName)
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(performedAtTimestampUtc)
        .exerciseActivities(Collections.emptyList())
        .build();

    given(workoutRepository.save(workoutEntityFromRequest))
        .willReturn(workoutEntityFromRequest);

    Workout expectedWorkout = Workout.builder()
        .name(workoutName)
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

    Optional<Workout> workout = workoutDataService.getWorkoutById(1L);

    verify(workoutRepository).findById(1L);
    verifyZeroInteractions(workoutConverter);
    assertFalse(workout.isPresent());
  }

  @Test
  public void getWorkoutById() {
    WorkoutEntity workoutEntity = createWorkoutEntity();

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
    given(workoutRepository.findByName(WORKOUT_NAME))
        .willReturn(Optional.empty());

    Optional<Workout> workout = workoutDataService.getWorkoutByName(WORKOUT_NAME);

    verify(workoutRepository).findByName(WORKOUT_NAME);
    verifyZeroInteractions(workoutConverter);
    assertFalse(workout.isPresent());
  }

  @Test
  public void getWorkoutByName() {
    WorkoutEntity workoutEntity = createWorkoutEntity();

    given(workoutRepository.findByName(WORKOUT_NAME))
        .willReturn(Optional.of(workoutEntity));

    given(workoutConverter.convert(workoutEntity))
        .willReturn(Workout.builder().build());

    Optional<Workout> workout = workoutDataService.getWorkoutByName(WORKOUT_NAME);

    verify(workoutRepository).findByName(WORKOUT_NAME);
    verify(workoutConverter).convert(workoutEntity);
    assertTrue(workout.isPresent());
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

    workoutDataService.updateSets(workoutId, exerciseActivity);

    verify(exerciseActivityConverter).convert(exerciseActivity);
    verify(exerciseActivityRepository).save(exerciseActivityEntity);
  }

  @Test
  public void addExerciseActivity_exerciseNotFound() {
    long exerciseId = 1L;
    long workoutId = 1L;

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> workoutDataService.addExerciseActivity(workoutId, exerciseId));

    verify(exerciseRepository).findById(exerciseId);
    verifyZeroInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void addExerciseActivity_workoutNotFound() {
    long exerciseId = 1L;
    long workoutId = 1L;

    ExerciseEntity exerciseEntity = createExerciseEntity(exerciseId);

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.of(exerciseEntity));

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> workoutDataService.addExerciseActivity(workoutId, exerciseId));

    verify(exerciseRepository).findById(exerciseId);
    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void addExerciseActivity() throws WorkoutNotFoundException, ExerciseNotFoundException {
    long exerciseId = 1L;
    long workoutId = 1L;

    ExerciseEntity exerciseEntity = createExerciseEntity(exerciseId);
    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.of(exerciseEntity));

    WorkoutEntity workoutEntity = createWorkoutEntity().toBuilder()
        .exerciseActivities(Collections.emptyList())
        .build();
    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    ExerciseActivityEntity newExerciseActivity = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();

    WorkoutEntity updatedWorkoutEntity = workoutEntity.toBuilder()
        .exerciseActivities(Collections.singletonList(newExerciseActivity))
        .build();

    given(workoutRepository.save(updatedWorkoutEntity))
        .willReturn(updatedWorkoutEntity);

    Exercise exercise = createExercise();
    ExerciseActivity exerciseActivity = createEmptyExerciseActivity(exercise);

    given(exerciseActivityConverter.convert(newExerciseActivity))
        .willReturn(exerciseActivity);

    ExerciseActivity actualExerciseActivity = workoutDataService.addExerciseActivity(workoutId, exerciseId);
    assertEquals(exerciseActivity, actualExerciseActivity);

    verify(exerciseRepository).findById(exerciseId);
    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).save(updatedWorkoutEntity);
    verify(exerciseActivityConverter).convert(newExerciseActivity);
  }

  @Test
  public void deleteExerciseActivity_workoutNotFound() throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () -> workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity_noExerciseActivities() {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    WorkoutEntity workoutEntity = createWorkoutEntity();

    workoutEntity.setExerciseActivities(Collections.emptyList());

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    assertThrows(ExerciseActivityNotFoundException.class, () -> workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity_exerciseActivityNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    WorkoutEntity workoutEntity = createWorkoutEntity();

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(1L);
    workoutEntity.setExerciseActivities(Collections.singletonList(exerciseActivityEntity));

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    assertThrows(ExerciseActivityNotFoundException.class, () -> workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity() throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    WorkoutEntity workoutEntity = createWorkoutEntity();

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);
    workoutEntity.setExerciseActivities(Collections.singletonList(exerciseActivityEntity));

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    ExerciseActivity expectedExerciseActivity = ExerciseActivity.builder()
        .id(exerciseActivityId)
        .build();

    given(exerciseActivityConverter.convert(exerciseActivityEntity))
        .willReturn(expectedExerciseActivity);

    ExerciseActivity actualExerciseActivity = workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId);

    assertEquals(expectedExerciseActivity.getId(), actualExerciseActivity.getId());

    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).save(any(WorkoutEntity.class));
    verify(exerciseActivityConverter).convert(exerciseActivityEntity);
  }

  private Workout createWorkout() {
    return Workout.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();
  }

  private WorkoutEntity createWorkoutEntity() {
    return WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
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

  private ExerciseActivity createEmptyExerciseActivity(Exercise exercise) {
    return ExerciseActivity.builder()
        .exercise(exercise)
        .sets(Collections.emptyList())
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
}