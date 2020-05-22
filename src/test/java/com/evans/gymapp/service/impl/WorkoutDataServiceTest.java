package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.*;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.service.impl.WorkoutDataService.ResourceNotFoundException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutDataServiceTest {

  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  private IWorkoutDataService workoutDataService;

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

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .workout(workoutEntity)
        .sets(Collections.emptyList())
        .build();

    ExerciseActivity expectedExerciseActivity = ExerciseActivity.builder()
        .exercise(createExercise())
        .sets(Collections.emptyList())
        .build();
    given(exerciseActivityConverter.convert(exerciseActivityEntity))
        .willReturn(expectedExerciseActivity);

    given(exerciseActivityRepository.save(exerciseActivityEntity))
        .willReturn(exerciseActivityEntity);

    ExerciseActivity exerciseActivity = workoutDataService.addExerciseActivity(workoutId, exerciseId);

    assertEquals(expectedExerciseActivity, exerciseActivity);
    verify(exerciseRepository).findById(exerciseId);
    verify(workoutRepository).findById(workoutId);
    verify(exerciseActivityConverter).convert(exerciseActivityEntity);
    verify(exerciseActivityRepository).save(exerciseActivityEntity);
  }

  @Test
  public void deleteExerciseActivity_exerciseActivityNotFound() {
    long exerciseActivityId = 2L;

    assertThrows(ExerciseActivityNotFoundException.class,
        () -> workoutDataService.deleteExerciseActivity(exerciseActivityId));

    verify(exerciseActivityRepository).findById(exerciseActivityId);
    verifyNoMoreInteractions(exerciseActivityRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity() throws ExerciseActivityNotFoundException {
    long exerciseActivityId = 2L;

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);

    given(exerciseActivityRepository.findById(exerciseActivityId))
        .willReturn(Optional.of(exerciseActivityEntity));

    doNothing().when(exerciseActivityRepository).deleteById(exerciseActivityId);

    ExerciseActivity expectedExerciseActivity = createExerciseActivity(exerciseActivityId);

    given(exerciseActivityConverter.convert(exerciseActivityEntity))
        .willReturn(expectedExerciseActivity);

    ExerciseActivity exerciseActivity = workoutDataService.deleteExerciseActivity(exerciseActivityId);

    assertEquals(expectedExerciseActivity, exerciseActivity);
    verify(exerciseActivityRepository).findById(exerciseActivityId);
    verify(exerciseActivityRepository).deleteById(exerciseActivityId);
    verify(exerciseActivityConverter).convert(exerciseActivityEntity);
  }

  private Workout createWorkout() {
    return Workout.builder()
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();
  }

  private WorkoutEntity createWorkoutEntity() {
    return WorkoutEntity.builder()
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

  private ExerciseActivity createExerciseActivity(long exerciseActivityId) {
    Exercise exercise = createExercise();

    return ExerciseActivity.builder()
        .exercise(exercise)
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