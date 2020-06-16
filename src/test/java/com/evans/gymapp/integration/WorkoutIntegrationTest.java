package com.evans.gymapp.integration;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.evans.gymapp.util.EndpointConstants.WORKOUTS;
import static com.evans.gymapp.util.EndpointConstants.WORKOUT_BY_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
public class WorkoutIntegrationTest extends IntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private WorkoutRepository workoutRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  private static final String MUST_NOT_BE_NULL = "must not be null";

  private static final ParameterizedTypeReference<Map<String, String>> MAP_STRING_STRING_TYPE =
      new ParameterizedTypeReference<Map<String, String>>() {
      };

  @BeforeEach
  public void setUp() {
    workoutRepository.deleteAll();

    exerciseRepository.deleteAll();
  }

  @Test
  public void getAllWorkouts_noWorkouts() {
    getWorkouts()
        .expectStatus().isOk()
        .expectBodyList(Workout.class)
        .hasSize(0);
  }

  @Test
  public void getAllWorkouts() {
    IntStream.range(0, 3)
        .mapToObj(i -> createWorkout())
        .forEach(workoutRepository::save);

    getWorkouts()
        .expectStatus().isOk()
        .expectBodyList(Workout.class)
        .hasSize(3);
  }

  @Test
  public void getWorkoutById_noWorkout() {
    getWorkoutById(3L)
        .expectStatus().isNotFound();
  }

  @Test
  public void getWorkoutById() {
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkout());

    Workout workoutById = getWorkoutById(savedWorkout.getId())
        .expectStatus().isOk()
        .expectBody(Workout.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(workoutById);
    assertEquals(savedWorkout.getId(), workoutById.getId());
  }

  @Test
  public void addWorkout_invalidRequest() {
    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutType(null)
        .performedAtTimestampUtc(null)
        .build();

    Map<String, String> errors = addWorkout(request)
        .expectStatus().isBadRequest()
        .expectBody(MAP_STRING_STRING_TYPE)
        .returnResult()
        .getResponseBody();

    assertNotNull(errors);
    assertEquals(2, errors.size());
    assertThat(errors, hasEntry("workoutType", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("performedAtTimestampUtc", MUST_NOT_BE_NULL));
  }

  @Test
  public void addWorkout() {
    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .build();

    Workout addedWorkout = addWorkout(request)
        .expectStatus().isOk()
        .expectBody(Workout.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(addedWorkout);
    assertEquals(request.getWorkoutType(), addedWorkout.getWorkoutType());
    assertEquals(request.getPerformedAtTimestampUtc(), addedWorkout.getPerformedAtTimestampUtc());
    assertTrue(addedWorkout.getExerciseActivities().isEmpty());
    assertNull(addedWorkout.getNotes());

    Optional<WorkoutEntity> byId = workoutRepository.findById(addedWorkout.getId());
    assertTrue(byId.isPresent());
  }

  @Test
  public void editWorkout_invalidRequest() {
    long workoutId = 1L;

    EditWorkoutRequest request = EditWorkoutRequest.builder()
        .workoutType(null)
        .performedAtTimestampUtc(null)
        .build();

    Map<String, String> errors = editWorkout(workoutId, request)
        .expectStatus().isBadRequest()
        .expectBody(MAP_STRING_STRING_TYPE)
        .returnResult()
        .getResponseBody();

    assertNotNull(errors);
    assertEquals(2, errors.size());
    assertThat(errors, hasEntry("workoutType", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("performedAtTimestampUtc", MUST_NOT_BE_NULL));
  }

  @Test
  public void editWorkout_workoutNotFound() {
    long workoutId = 1L;

    EditWorkoutRequest request = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .build();

    editWorkout(workoutId, request)
        .expectStatus().isNotFound();
  }

  @Test
  public void editWorkout() {
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkout());
    long workoutId = savedWorkout.getId();

    EditWorkoutRequest request = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .build();

    Workout returnedEditedWorkout = editWorkout(workoutId, request)
        .expectStatus().isOk()
        .expectBody(Workout.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(returnedEditedWorkout);
    assertEquals(workoutId, returnedEditedWorkout.getId());
    assertEquals(request.getWorkoutType(), returnedEditedWorkout.getWorkoutType());
    assertEquals(request.getPerformedAtTimestampUtc(), returnedEditedWorkout.getPerformedAtTimestampUtc());

    // Assert that the returned and stored workout are equal
    Optional<WorkoutEntity> storedEditedWorkout = workoutRepository.findById(workoutId);
    assertTrue(storedEditedWorkout.isPresent());
    assertEquals(returnedEditedWorkout.getWorkoutType(), storedEditedWorkout.get().getWorkoutType());
    assertEquals(returnedEditedWorkout.getPerformedAtTimestampUtc(), storedEditedWorkout.get().getPerformedAtTimestampUtc());
  }

  @Test
  public void deleteWorkout_workoutNotFound() {
    long workoutId = 1L;

    deleteWorkout(workoutId)
        .expectStatus().isNotFound();
  }

  @Test
  public void deleteWorkout() {
    WorkoutEntity savedWorkout = workoutRepository.save(createWorkout());
    long workoutId = savedWorkout.getId();

    deleteWorkout(workoutId)
        .expectStatus().isOk()
        .expectBody(Workout.class);

    assertFalse(workoutRepository.existsById(workoutId));
  }

  private ResponseSpec getWorkouts() {
    return webTestClient.get()
        .uri(WORKOUTS)
        .exchange();
  }

  private ResponseSpec getWorkoutById(long workoutId) {
    return webTestClient.get()
        .uri(WORKOUT_BY_ID, workoutId)
        .exchange();
  }

  private ResponseSpec addWorkout(CreateWorkoutRequest request) {
    return webTestClient.post()
        .uri(WORKOUTS)
        .syncBody(request)
        .exchange();
  }

  private ResponseSpec editWorkout(long workoutId, EditWorkoutRequest request) {
    return webTestClient.patch()
        .uri(WORKOUT_BY_ID, workoutId)
        .syncBody(request)
        .exchange();
  }

  private ResponseSpec deleteWorkout(long workoutId) {
    return webTestClient.delete()
        .uri(WORKOUT_BY_ID, workoutId)
        .exchange();
  }

  private WorkoutEntity createWorkout() {
    return WorkoutEntity.builder()
        .workoutType(WorkoutType.PULL)
        .performedAtTimestampUtc(Instant.now())
        .exerciseActivities(new ArrayList<>())
        .build();
  }
}

