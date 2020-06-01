package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.*;
import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import com.evans.gymapp.util.converter.ExerciseSetConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseActivityDataServiceTest {

  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  private IExerciseActivityDataService exerciseActivityDataService;

  @Mock
  private WorkoutRepository workoutRepository;

  @Mock
  private ExerciseRepository exerciseRepository;

  @Mock
  private ExerciseActivityConverter exerciseActivityConverter;

  @Mock
  private ExerciseSetConverter exerciseSetConverter;

  @BeforeEach
  public void setUp() {
    exerciseActivityDataService = new ExerciseActivityDataService(workoutRepository, exerciseRepository,
        exerciseActivityConverter, exerciseSetConverter);
  }

  @Test
  public void addExerciseActivity_exerciseNotFound() {
    long exerciseId = 1L;
    long workoutId = 1L;

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.empty());

    assertThrows(ExerciseNotFoundException.class, () -> exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId));

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

    assertThrows(WorkoutNotFoundException.class, () -> exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId));

    verify(exerciseRepository).findById(exerciseId);
    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void addExerciseActivity() throws WorkoutNotFoundException, ExerciseNotFoundException {
    long exerciseId = 1L;
    long workoutId = 1L;

    WorkoutEntity workout = createWorkoutEntity(workoutId).toBuilder()
        .exerciseActivities(new ArrayList<>())
        .build();

    ExerciseEntity exerciseEntity = createExerciseEntity(exerciseId);

    ExerciseActivityEntity newExerciseActivity = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();

    given(exerciseRepository.findById(exerciseId))
        .willReturn(Optional.of(exerciseEntity));

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workout));

    WorkoutEntity updatedWorkout = workout.toBuilder()
        .exerciseActivities(Collections.singletonList(newExerciseActivity))
        .build();

    given(workoutRepository.save(updatedWorkout))
        .willReturn(updatedWorkout);

    ExerciseActivity expectedExerciseActivity = ExerciseActivity.builder()
        .exercise(createExercise())
        .sets(Collections.emptyList())
        .build();

    given(exerciseActivityConverter.convert(newExerciseActivity))
        .willReturn(expectedExerciseActivity);

    ExerciseActivity exerciseActivity = exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId);

    assertEquals(expectedExerciseActivity, exerciseActivity);
    verify(exerciseRepository).findById(exerciseId);
    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).save(updatedWorkout);
    verify(exerciseActivityConverter).convert(newExerciseActivity);
  }

  @Test
  public void deleteExerciseActivity_workoutNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    assertThrows(WorkoutNotFoundException.class,
        () -> exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity_exerciseActivityNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(createWorkoutEntity(workoutId)));

    assertThrows(ExerciseActivityNotFoundException.class,
        () -> exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId));

    verify(workoutRepository).findById(workoutId);
    verifyNoMoreInteractions(workoutRepository);
    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void deleteExerciseActivity() throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);

    WorkoutEntity workout = createWorkoutEntity(workoutId).toBuilder()
        .exerciseActivities(new ArrayList<>(Collections.singletonList(exerciseActivityEntity)))
        .build();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workout));

    WorkoutEntity updatedWorkoutEntity = workout.toBuilder()
        .exerciseActivities(Collections.emptyList())
        .build();

    ExerciseActivity expectedExerciseActivity = createExerciseActivity(exerciseActivityId);

    given(exerciseActivityConverter.convert(exerciseActivityEntity))
        .willReturn(expectedExerciseActivity);

    ExerciseActivity deletedExerciseActivity =
        exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId);

    assertEquals(expectedExerciseActivity, deletedExerciseActivity);
    verify(workoutRepository).findById(workoutId);
    verify(workoutRepository).save(updatedWorkoutEntity);
    verify(exerciseActivityConverter).convert(exerciseActivityEntity);
  }

  @Test
  public void updateSets_workoutNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId);

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.empty());

    assertThrows(WorkoutNotFoundException.class, () ->
        exerciseActivityDataService.updateSets(workoutId, exerciseActivity));

    verify(workoutRepository).findById(workoutId);
    verifyZeroInteractions(exerciseSetConverter);
    verifyNoMoreInteractions(workoutRepository);
  }

  @Test
  public void updateSets_exerciseActivityNotFound_noExerciseActivities() {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId);

    WorkoutEntity workoutEntity = createWorkoutEntity(workoutId);

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    assertThrows(ExerciseActivityNotFoundException.class,
        () -> exerciseActivityDataService.updateSets(workoutId, exerciseActivity));

    verify(workoutRepository).findById(workoutId);
    verifyZeroInteractions(exerciseSetConverter);
    verifyNoMoreInteractions(workoutRepository);
  }

  @Test
  public void updateSets_exerciseActivityNotFound_noMatchingIds() {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId);

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(2L);

    WorkoutEntity workoutEntity = createWorkoutEntity(workoutId)
        .toBuilder()
        .exerciseActivities(Collections.singletonList(exerciseActivityEntity))
        .build();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    assertThrows(ExerciseActivityNotFoundException.class,
        () -> exerciseActivityDataService.updateSets(workoutId, exerciseActivity));

    verify(workoutRepository).findById(workoutId);
    verifyZeroInteractions(exerciseSetConverter);
    verifyNoMoreInteractions(workoutRepository);
  }

  @Test
  public void updateSets_noSets() throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId)
        .toBuilder()
        .sets(Collections.emptyList())
        .build();

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivityEntity(exerciseActivityId);

    WorkoutEntity workoutEntity = createWorkoutEntity(workoutId)
        .toBuilder()
        .exerciseActivities(Collections.singletonList(exerciseActivityEntity))
        .build();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);

    verify(workoutRepository).findById(workoutId);
    verifyZeroInteractions(exerciseSetConverter);
    verify(workoutRepository).save(workoutEntity);
  }

  @Test
  public void updateSets() throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    long workoutId = 1L;
    long exerciseActivityId = 2L;

    ExerciseSet weightedSet = createWeightedSet();

    ExerciseActivity exerciseActivity = createExerciseActivity(exerciseActivityId)
        .toBuilder()
        .sets(Collections.singletonList(weightedSet))
        .build();

    List<ExerciseActivityEntity> exerciseActivities = new ArrayList<>(Arrays.asList(
        createExerciseActivityEntity(1L),
        createExerciseActivityEntity(3L)
    ));

    int exerciseActivityIndex = 1;
    exerciseActivities.add(exerciseActivityIndex, createExerciseActivityEntity(exerciseActivityId));

    WorkoutEntity workoutEntity = createWorkoutEntity(workoutId)
        .toBuilder()
        .exerciseActivities(exerciseActivities)
        .build();

    given(workoutRepository.findById(workoutId))
        .willReturn(Optional.of(workoutEntity));

    ExerciseSetEntity weightedSetEntity = createWeightedSetEntity();

    given(exerciseSetConverter.convert(weightedSet))
        .willReturn(weightedSetEntity);

    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);

    ExerciseActivityEntity updatedExerciseActivity = exerciseActivities.get(exerciseActivityIndex)
        .toBuilder()
        .sets(Collections.singletonList(weightedSetEntity))
        .build();

    WorkoutEntity updatedWorkoutEntity = workoutEntity.toBuilder()
        .exerciseActivities(Arrays.asList(
            createExerciseActivityEntity(1L),
            updatedExerciseActivity,
            createExerciseActivityEntity(3L)))
        .build();

    verify(workoutRepository).findById(workoutId);
    verify(exerciseSetConverter).convert(weightedSet);
    verify(workoutRepository).save(updatedWorkoutEntity);
    verify(workoutRepository).save(argThat(workout -> {
      List<ExerciseSetEntity> sets = workout.getExerciseActivities().get(exerciseActivityIndex).getSets();

      return !sets.isEmpty() && verifyAllSetsHaveExerciseActivityId(sets, exerciseActivityId);
    }));
  }

  private boolean verifyAllSetsHaveExerciseActivityId(List<ExerciseSetEntity> sets, long exerciseActivityId) {
    return sets.stream()
        .map(ExerciseSetEntity::getExerciseActivity)
        .map(ExerciseActivityEntity::getId)
        .allMatch(id -> id != null && id.equals(exerciseActivityId));
  }

  private WorkoutEntity createWorkoutEntity(long workoutId) {
    return WorkoutEntity.builder()
        .id(workoutId)
        .workoutType(WorkoutType.PULL)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(Instant.now())
        .notes("Notes")
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

  private WeightedSetEntity createWeightedSetEntity() {
    return WeightedSetEntity.builder()
        .weightKg(1D)
        .numberOfReps(4)
        .status(Status.FAILED)
        .build();
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
}