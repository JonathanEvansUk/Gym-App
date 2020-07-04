package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private IWorkoutDataService workoutDataService;

  private static final TypeReference<Map<String, String>> MAP_STRING_STRING_TYPE = new TypeReference<Map<String, String>>() {
  };

  private static final String MUST_NOT_BE_NULL = "must not be null";

  @Test
  public void getAllWorkouts() throws Exception {
    Instant now = Instant.now();

    Workout workout = Workout.builder()
        .id(1L)
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(now)
        .build();

    given(workoutDataService.getAllWorkouts())
        .willReturn(Collections.singletonList(workout));

    MockHttpServletResponse response = mockMvc.perform(get("/workouts"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    List<Workout> workouts = Arrays.asList(objectMapper.readValue(response.getContentAsString(), Workout[].class));

    assertFalse(workouts.isEmpty());
    assertEquals(workout, workouts.get(0));

    verify(workoutDataService).getAllWorkouts();
  }

  @Test
  public void getWorkoutById_noWorkout() throws Exception {
    long workoutId = 1L;

    given(workoutDataService.getWorkoutById(workoutId))
        .willThrow(new WorkoutNotFoundException(workoutId));

    mockMvc.perform(get("/workouts/{workoutId}", workoutId))
        .andExpect(status().isNotFound());

    verify(workoutDataService).getWorkoutById(workoutId);
  }

  @Test
  public void getWorkoutById() throws Exception {
    Instant now = Instant.now();

    Workout workout = Workout.builder()
        .id(1L)
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptyList())
        .performedAtTimestampUtc(now)
        .build();

    given(workoutDataService.getWorkoutById(1L))
        .willReturn(workout);

    MockHttpServletResponse response = mockMvc.perform(get("/workouts/{workoutId}", 1L))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    Workout workoutById = objectMapper.readValue(response.getContentAsString(), Workout.class);

    assertEquals(workout, workoutById);

    verify(workoutDataService).getWorkoutById(1L);
  }

  @Test
  public void addWorkout_invalidRequest() throws Exception {
    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutType(null)
        .performedAtTimestampUtc(null)
        .build();

    String jsonRequest = objectMapper.writeValueAsString(request);

    MockHttpServletResponse response = mockMvc.perform(post("/workouts/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    Map<String, String> errors = objectMapper.readValue(response.getContentAsString(), MAP_STRING_STRING_TYPE);

    assertEquals(2, errors.size());
    assertThat(errors, hasEntry("workoutType", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("performedAtTimestampUtc", MUST_NOT_BE_NULL));

    verifyZeroInteractions(workoutDataService);
  }

  @Test
  public void addWorkout() throws Exception {
    Instant now = Instant.now();

    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    Workout workout = Workout.builder()
        .id(1L)
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    given(workoutDataService.addWorkout(request))
        .willReturn(workout);

    String jsonRequest = objectMapper.writeValueAsString(request);

    MockHttpServletResponse response = mockMvc.perform(post("/workouts/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    Workout returnedWorkout = objectMapper.readValue(response.getContentAsString(), Workout.class);

    assertEquals(workout, returnedWorkout);

    verify(workoutDataService).addWorkout(request);
  }

  @Test
  public void editWorkout_invalidRequest() throws Exception {
    long workoutId = 1L;

    EditWorkoutRequest request = EditWorkoutRequest.builder()
        .workoutType(null)
        .performedAtTimestampUtc(null)
        .build();

    String jsonRequest = objectMapper.writeValueAsString(request);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    Map<String, String> errors = objectMapper.readValue(response.getContentAsString(), MAP_STRING_STRING_TYPE);

    assertEquals(2, errors.size());
    assertThat(errors, hasEntry("workoutType", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("performedAtTimestampUtc", MUST_NOT_BE_NULL));

    verifyZeroInteractions(workoutDataService);
  }

  @Test
  public void editWorkout_workoutNotFound() throws Exception {
    long workoutId = 1L;

    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(Instant.now())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(editWorkoutRequest);

    given(workoutDataService.editWorkout(workoutId, editWorkoutRequest))
        .willThrow(new WorkoutNotFoundException(workoutId));

    mockMvc.perform(patch("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    verify(workoutDataService).editWorkout(workoutId, editWorkoutRequest);
  }

  @Test
  public void editWorkout() throws Exception {
    long workoutId = 1L;

    Instant now = Instant.now();

    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(now)
        .build();

    Workout expectedWorkout = Workout.builder()
        .id(workoutId)
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(now)
        .build();

    given(workoutDataService.editWorkout(workoutId, editWorkoutRequest))
        .willReturn(expectedWorkout);

    String jsonRequest = objectMapper.writeValueAsString(editWorkoutRequest);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    Workout editedWorkout = objectMapper.readValue(response.getContentAsString(), Workout.class);

    assertEquals(expectedWorkout, editedWorkout);

    verify(workoutDataService).editWorkout(workoutId, editWorkoutRequest);
  }

  @Test
  public void deleteWorkout_workoutNotFound() throws Exception {
    long workoutId = 1L;

    willThrow(new WorkoutNotFoundException(workoutId))
        .given(workoutDataService).deleteWorkout(workoutId);

    mockMvc.perform(delete("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    verify(workoutDataService).deleteWorkout(workoutId);
  }

  @Test
  public void deleteWorkout() throws Exception {
    long workoutId = 1L;

    doNothing().when(workoutDataService).deleteWorkout(workoutId);

    mockMvc.perform(delete("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    verify(workoutDataService).deleteWorkout(workoutId);
  }
}