package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WorkoutRepositoryTest {

  @Autowired
  private WorkoutRepository workoutRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  private static final ExerciseEntity BICEP_CURL = ExerciseEntity.builder()
      .id(1L)
      .muscleGroup(MuscleGroup.BICEP)
      .name("Bicep Curl")
      .information("Information")
      .build();

  @BeforeEach
  public void addBicepCurl() {
    exerciseRepository.save(BICEP_CURL);
  }

  @Test
  public void save_thenFindById() {
    WorkoutEntity workout = createPopulatedWorkout();

    WorkoutEntity savedWorkout = workoutRepository.save(workout);

    assertEquals(workout, savedWorkout);

    Optional<WorkoutEntity> byId = workoutRepository.findById(savedWorkout.getId());

    assertTrue(byId.isPresent());
    assertEquals(workout, byId.get());
  }

  @Test
  public void findAll() {
    IntStream.range(0, 3)
        .mapToObj(i -> createPopulatedWorkout())
        .forEach(workoutRepository::save);

    List<WorkoutEntity> allWorkouts = workoutRepository.findAll();

    assertEquals(3, allWorkouts.size());
  }

  @Test
  public void delete() {
    WorkoutEntity workout = createPopulatedWorkout();

    WorkoutEntity savedWorkout = workoutRepository.save(workout);

    Optional<WorkoutEntity> byId = workoutRepository.findById(savedWorkout.getId());
    assertTrue(byId.isPresent());

    workoutRepository.delete(workout);

    Optional<WorkoutEntity> byIdPostDelete = workoutRepository.findById(savedWorkout.getId());
    assertFalse(byIdPostDelete.isPresent());
  }

  @Test
  public void addPolymorphicSetTypes() {
    List<ExerciseSetEntity> sets = Arrays.asList(createWeightedSet(), createNonWeightedSet());

    ExerciseActivityEntity exerciseActivityEntity = createExerciseActivity(sets);

    WorkoutEntity workoutEntity = createWorkout(exerciseActivityEntity);

    WorkoutEntity savedWorkout = workoutRepository.save(workoutEntity);

    assertNotNull(savedWorkout);
    assertNotNull(savedWorkout.getExerciseActivities());
    assertNotNull(savedWorkout.getExerciseActivities().get(0));

    List<ExerciseSetEntity> savedSets = savedWorkout.getExerciseActivities().get(0).getSets();

    assertNotNull(savedSets);
    assertEquals(2, savedSets.size());

    assertThat(savedSets.get(0), instanceOf(WeightedSetEntity.class));
    assertThat(savedSets.get(1), instanceOf(NonWeightedSetEntity.class));
  }

  private WorkoutEntity createPopulatedWorkout() {
    List<ExerciseSetEntity> sets = Arrays.asList(createNonWeightedSet(), createWeightedSet());

    ExerciseActivityEntity exerciseActivity = createExerciseActivity(sets);

    return createWorkout(exerciseActivity);
  }

  private WorkoutEntity createWorkout(ExerciseActivityEntity exerciseActivityEntity) {
    return WorkoutEntity.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .exerciseActivities(Collections.singletonList(exerciseActivityEntity))
        .notes("Notes")
        .build();
  }

  private ExerciseActivityEntity createExerciseActivity(List<ExerciseSetEntity> sets) {
    return ExerciseActivityEntity.builder()
        .exercise(BICEP_CURL)
        .sets(sets)
        .notes("Notes")
        .build();
  }

  private NonWeightedSetEntity createNonWeightedSet() {
    return NonWeightedSetEntity.builder()
        .weight("Red Band")
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();
  }

  private WeightedSetEntity createWeightedSet() {
    return WeightedSetEntity.builder()
        .weightKg(12D)
        .numberOfReps(8)
        .status(Status.COMPLETED)
        .build();
  }
}