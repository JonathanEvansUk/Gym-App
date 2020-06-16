package com.evans.gymapp.integration;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.*;

import static com.evans.gymapp.util.EndpointConstants.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
public class ExerciseActivityIntegrationTest extends IntegrationTest {

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private WorkoutRepository workoutRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  private static final ParameterizedTypeReference<Map<String, String>> MAP_STRING_STRING_TYPE =
      new ParameterizedTypeReference<Map<String, String>>() {
      };

  private static final String SETS = "sets";

  private static final String MUST_NOT_BE_NULL = "must not be null";

  @BeforeEach
  public void setUp() {
    workoutRepository.deleteAll();

    exerciseRepository.deleteAll();
  }

  @Test
  public void updateSets_invalidRequest() {
    ExerciseEntity bicepCurl = exerciseRepository.save(createBicepCurl());
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkoutWithExerciseActivity(bicepCurl));

    long workoutId = savedWorkout.getId();
    long exerciseActivityId = savedWorkout.getExerciseActivities().get(0).getId();

    ExerciseActivity exerciseActivityRequest = ExerciseActivity.builder()
        .id(exerciseActivityId)
        .sets(null)
        .build();

    Map<String, String> errors = updateSets(workoutId, exerciseActivityRequest)
        .expectStatus().isBadRequest()
        .expectBody(MAP_STRING_STRING_TYPE)
        .returnResult()
        .getResponseBody();

    assertNotNull(errors);
    assertEquals(1, errors.size());
    assertThat(errors, hasEntry(SETS, MUST_NOT_BE_NULL));
  }

  @Test
  public void updateSets_workoutNotFound() {
    long workoutId = 1L;

    ExerciseActivity exerciseActivityRequest = ExerciseActivity.builder()
        .id(2L)
        .sets(Collections.emptyList())
        .build();

    updateSets(workoutId, exerciseActivityRequest)
        .expectStatus().isNotFound();
  }

  @Test
  public void updateSets_exerciseActivityNotFound() {
    WorkoutEntity workoutEntity = createWorkout().toBuilder()
        .exerciseActivities(Collections.emptyList())
        .build();

    WorkoutEntity savedWorkout = workoutRepository.save(workoutEntity);

    ExerciseActivity exerciseActivityRequest = ExerciseActivity.builder()
        .id(2L)
        .sets(Collections.emptyList())
        .build();

    updateSets(savedWorkout.getId(), exerciseActivityRequest)
        .expectStatus().isNotFound();
  }

  @Test
  public void updateSets() {
    ExerciseEntity bicepCurl = exerciseRepository.save(createBicepCurl());
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkoutWithExerciseActivity(bicepCurl));

    long workoutId = savedWorkout.getId();
    long exerciseActivityId = savedWorkout.getExerciseActivities().get(0).getId();

    NonWeightedSet nonWeightedSet = createNonWeightedSet();
    WeightedSet weightedSet = createWeightedSet();

    ExerciseActivity exerciseActivityRequest = ExerciseActivity.builder()
        .id(exerciseActivityId)
        .sets(Arrays.asList(nonWeightedSet, weightedSet))
        .build();

    updateSets(workoutId, exerciseActivityRequest)
        .expectStatus().isOk();

    List<ExerciseSetEntity> savedSets = fetchSets(workoutId);

    assertEquals(2, savedSets.size());
    assertThat(savedSets.get(0), instanceOf(NonWeightedSetEntity.class));
    assertThat(savedSets.get(1), instanceOf(WeightedSetEntity.class));
  }

  private List<ExerciseSetEntity> fetchSets(long workoutId) {
    return transactionTemplate.execute((transactionStatus -> {

      Optional<List<ExerciseSetEntity>> sets = workoutRepository.findById(workoutId)
          .map(workout -> workout.getExerciseActivities().get(0).getSets());

      assertTrue(sets.isPresent());

      Hibernate.initialize(sets.get());

      return sets.get();
    }));
  }

  @Test
  public void addExerciseActivity_workoutNotFound() {
    long workoutId = 1L;
    long exerciseId = 2L;

    addExerciseActivity(workoutId, exerciseId)
        .expectStatus().isNotFound();
  }

  @Test
  public void addExerciseActivity_exerciseNotFound() {
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkout());

    long workoutId = savedWorkout.getId();
    long exerciseId = 29L;

    addExerciseActivity(workoutId, exerciseId)
        .expectStatus().isNotFound();
  }

  @Test
  public void addExerciseActivity() {
    ExerciseEntity bicepCurl = exerciseRepository.save(createBicepCurl());
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkout());

    long exerciseId = bicepCurl.getId();
    long workoutId = savedWorkout.getId();

    ExerciseActivity addedExerciseActivity = addExerciseActivity(workoutId, exerciseId)
        .expectStatus().isOk()
        .expectBody(ExerciseActivity.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(addedExerciseActivity);
    assertEquals(bicepCurl.getId(), addedExerciseActivity.getExercise().getId());
    assertEquals(Collections.emptyList(), addedExerciseActivity.getSets());

    ExerciseActivityEntity savedExerciseActivity = fetchExerciseActivity(savedWorkout.getId());

    assertNotNull(savedExerciseActivity);
    assertEquals(addedExerciseActivity.getId(), savedExerciseActivity.getId());
    assertEquals(Collections.emptyList(), savedExerciseActivity.getSets());
  }

  private ExerciseActivityEntity fetchExerciseActivity(long workoutId) {
    return transactionTemplate.execute((transactionStatus) -> {
      Optional<ExerciseActivityEntity> exerciseActivity = workoutRepository.findById(workoutId)
          .map(WorkoutEntity::getExerciseActivities)
          .map(exerciseActivities -> exerciseActivities.get(0));

      assertTrue(exerciseActivity.isPresent());

      Hibernate.initialize(exerciseActivity.get().getSets());

      return exerciseActivity.get();
    });
  }

  @Test
  public void deleteExerciseActivity_workoutNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 3L;

    deleteExerciseActivity(workoutId, exerciseActivityId)
        .expectStatus().isNotFound();
  }

  @Test
  public void deleteExerciseActivity_exerciseActivityNotFound() {
    long workoutId = 1L;
    long exerciseActivityId = 3L;

    workoutRepository.save(createWorkout());

    deleteExerciseActivity(workoutId, exerciseActivityId)
        .expectStatus().isNotFound();
  }

  @Test
  public void deleteExerciseActivity() {
    ExerciseEntity bicepCurl = exerciseRepository.save(createBicepCurl());
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkoutWithExerciseActivity(bicepCurl));

    long workoutId = savedWorkout.getId();
    long exerciseActivityId = savedWorkout.getExerciseActivities().get(0).getId();

    ExerciseActivity deletedExerciseActivity = deleteExerciseActivity(workoutId, exerciseActivityId)
        .expectStatus().isOk()
        .expectBody(ExerciseActivity.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(deletedExerciseActivity);
    assertEquals(Long.valueOf(exerciseActivityId), deletedExerciseActivity.getId());

    List<ExerciseActivityEntity> exerciseActivities = fetchExerciseActivities(workoutId);
    assertTrue(exerciseActivities.isEmpty());
  }

  private List<ExerciseActivityEntity> fetchExerciseActivities(long workoutId) {
    return transactionTemplate.execute((transactionStatus -> {
      Optional<List<ExerciseActivityEntity>> exerciseActivityEntities = workoutRepository.findById(workoutId)
          .map(WorkoutEntity::getExerciseActivities);

      assertTrue(exerciseActivityEntities.isPresent());

      return exerciseActivityEntities.get();
    }));
  }

  private WeightedSet createWeightedSet() {
    return WeightedSet.builder()
        .weightKg(10D)
        .numberOfReps(8)
        .status(Status.FAILED)
        .build();
  }

  private NonWeightedSet createNonWeightedSet() {
    return NonWeightedSet.builder()
        .weight("Red band")
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();
  }

  private ResponseSpec updateSets(long workoutId, ExerciseActivity exerciseActivityRequest) {
    return webTestClient.patch()
        .uri(WORKOUT_BY_ID + UPDATE_SETS, workoutId)
        .syncBody(exerciseActivityRequest)
        .exchange();
  }

  private ResponseSpec addExerciseActivity(long workoutId, long exerciseId) {
    return webTestClient.post()
        .uri(WORKOUT_BY_ID + ADD_EXERCISE_ACTIVITY, workoutId)
        .syncBody(exerciseId)
        .exchange();
  }

  private ResponseSpec deleteExerciseActivity(long workoutId, long exerciseActivityId) {
    return webTestClient.delete()
        .uri(WORKOUT_BY_ID + DELETE_EXERCISE_ACTIVITY, workoutId, exerciseActivityId)
        .exchange();
  }

  private WorkoutEntity createWorkout() {
    return WorkoutEntity.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .exerciseActivities(new ArrayList<>())
        .build();
  }

  private WorkoutEntity createWorkoutWithExerciseActivity(ExerciseEntity exerciseEntity) {
    WorkoutEntity workout = createWorkout();

    workout.addExerciseActivity(createExerciseActivity(exerciseEntity));

    return workout;
  }

  private ExerciseActivityEntity createExerciseActivity(ExerciseEntity exerciseEntity) {
    return ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();
  }

  private ExerciseEntity createBicepCurl() {
    return ExerciseEntity.builder()
        .muscleGroup(MuscleGroup.BICEP)
        .name("Bicep Curl")
        .information("Information")
        .build();
  }
}
