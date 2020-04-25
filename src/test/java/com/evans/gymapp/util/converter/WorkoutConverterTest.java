package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.Workout.WorkoutBuilder;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity.WorkoutEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;

import static com.evans.gymapp.domain.MuscleGroup.BICEP;
import static com.evans.gymapp.domain.WorkoutType.LEGS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class WorkoutConverterTest {

  private WorkoutConverter workoutConverter;

  @Mock
  private ExerciseActivityConverter exerciseActivityConverter;

  private static final long EXERCISE_ID = 1L;
  private static final String EXERCISE_NAME = "Bicep Curl";
  private static final String EXERCISE_INFORMATION = "Bicep Curl with Dumbbell";

  private static final long WORKOUT_ID = 1L;
  private static final String NOTES = "Notes";

  private static final long EXERCISE_ACTIVITY_ID = 1L;
  private static final String EXERCISE_ACTIVITY_NOTES = "Notes";

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

  private static final ExerciseActivityEntity EXERCISE_ACTIVITY_ENTITY = ExerciseActivityEntity.builder()
      .id(EXERCISE_ACTIVITY_ID)
      .exercise(EXERCISE_ENTITY)
      .sets(Collections.emptyList())
      .notes(EXERCISE_ACTIVITY_NOTES)
      .build();

  private static final ExerciseActivity EXERCISE_ACTIVITY = ExerciseActivity.builder()
      .id(EXERCISE_ACTIVITY_ID)
      .exercise(EXERCISE)
      .sets(Collections.emptyList())
      .notes(EXERCISE_ACTIVITY_NOTES)
      .build();

  @BeforeEach
  public void setUp() {
    workoutConverter = new WorkoutConverter(exerciseActivityConverter);
  }

  @Test
  public void convertToEntity_noExerciseActivities() {
    Instant now = Instant.now();

    Workout workout = createWorkout(now)
        .exerciseActivities(Collections.emptyList())
        .build();

    WorkoutEntity expectedWorkoutEntity = createWorkoutEntity(now)
        .exerciseActivities(Collections.emptyList())
        .build();

    WorkoutEntity workoutEntity = workoutConverter.convert(workout);

    assertEquals(expectedWorkoutEntity, workoutEntity);

    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void convertToEntity() {
    Instant now = Instant.now();

    Workout workout = createWorkout(now)
        .exerciseActivities(Collections.singletonList(EXERCISE_ACTIVITY))
        .build();

    WorkoutEntity expectedWorkoutEntity = createWorkoutEntity(now)
        .exerciseActivities(Collections.singletonList(EXERCISE_ACTIVITY_ENTITY))
        .build();

    given(exerciseActivityConverter.convert(EXERCISE_ACTIVITY))
        .willReturn(EXERCISE_ACTIVITY_ENTITY);

    WorkoutEntity workoutEntity = workoutConverter.convert(workout);

    assertEquals(expectedWorkoutEntity, workoutEntity);

    verify(exerciseActivityConverter).convert(EXERCISE_ACTIVITY);
  }

  @Test
  public void convertFromEntity_noExerciseActivities() {
    Instant now = Instant.now();

    WorkoutEntity workoutEntity = createWorkoutEntity(now)
        .exerciseActivities(Collections.emptyList())
        .build();

    Workout expectedWorkout = createWorkout(now)
        .exerciseActivities(Collections.emptyList())
        .build();

    Workout workout = workoutConverter.convert(workoutEntity);

    assertEquals(expectedWorkout, workout);

    verifyZeroInteractions(exerciseActivityConverter);
  }

  @Test
  public void convertFromEntity() {
    Instant now = Instant.now();

    WorkoutEntity workoutEntity = createWorkoutEntity(now)
        .exerciseActivities(Collections.singletonList(EXERCISE_ACTIVITY_ENTITY))
        .build();

    Workout expectedWorkout = createWorkout(now)
        .exerciseActivities(Collections.singletonList(EXERCISE_ACTIVITY))
        .build();

    given(exerciseActivityConverter.convert(EXERCISE_ACTIVITY_ENTITY))
        .willReturn(EXERCISE_ACTIVITY);

    Workout workout = workoutConverter.convert(workoutEntity);

    assertEquals(expectedWorkout, workout);

    verify(exerciseActivityConverter).convert(EXERCISE_ACTIVITY_ENTITY);
  }

  private WorkoutEntityBuilder createWorkoutEntity(Instant now) {
    return WorkoutEntity.builder()
        .id(WORKOUT_ID)
        .workoutType(LEGS)
        .performedAtTimestampUtc(now)
        .notes(NOTES);
  }

  private WorkoutBuilder createWorkout(Instant now) {
    return Workout.builder()
        .id(WORKOUT_ID)
        .workoutType(LEGS)
        .performedAtTimestampUtc(now)
        .notes(NOTES);
  }
}