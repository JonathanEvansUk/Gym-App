package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
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
import java.util.*;

import static com.evans.gymapp.service.impl.WorkoutDataService.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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

  @Test
  public void getAllWorkouts() throws Exception {
    Instant now = Instant.now();

    Workout workout = Workout.builder()
        .id(1L)
        .name("name")
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
    given(workoutDataService.getWorkoutById(1L))
        .willThrow(new ResourceNotFoundException());

    mockMvc.perform(get("/workouts/{workoutId}", 1L))
        .andExpect(status().isNotFound());

    verify(workoutDataService).getWorkoutById(1L);
  }

  @Test
  public void getWorkoutById() throws Exception {
    Instant now = Instant.now();

    Workout workout = Workout.builder()
        .id(1L)
        .name("name")
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

  //TODO this test will probably be deleted as workoutName may become redundant
  @Test
  public void addWorkout_invalidRequest_blankName() throws Exception {

    String BLANK_STRING = "";

    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutName(BLANK_STRING)
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(Instant.now())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(request);

    MockHttpServletResponse response = mockMvc.perform(post("/workouts/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();


    TypeReference<HashMap<String, String>> typeReference = new TypeReference<HashMap<String, String>>() {
    };

    Map<String, String> validationErrors = objectMapper.readValue(response.getContentAsString(), typeReference);

    assertTrue(validationErrors.containsKey("workoutName"));

    //TODO replace string constant with property maybe
    assertEquals("must not be blank", validationErrors.get("workoutName"));

    verifyZeroInteractions(workoutDataService);
  }

  @Test
  public void addWorkout() throws Exception {
    Instant now = Instant.now();

    CreateWorkoutRequest request = CreateWorkoutRequest.builder()
        .workoutName("name")
        .workoutType(WorkoutType.ABS)
        .performedAtTimestampUtc(now)
        .build();

    Workout workout = Workout.builder()
        .id(1L)
        .name("name")
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
  public void editWorkout_invalidRequest_blankName() throws Exception {

    String BLANK_STRING = "";

    long workoutId = 1L;

    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutName(BLANK_STRING)
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(Instant.now())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(editWorkoutRequest);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    TypeReference<HashMap<String, String>> typeReference = new TypeReference<HashMap<String, String>>() {
    };

    Map<String, String> validationErrors = objectMapper.readValue(response.getContentAsString(), typeReference);

    assertTrue(validationErrors.containsKey("workoutName"));

    //TODO replace string constant with property maybe
    assertEquals("must not be blank", validationErrors.get("workoutName"));

    verifyZeroInteractions(workoutDataService);
  }

  @Test
  public void editWorkout_workoutNotFound() throws Exception {
    long workoutId = 1L;

    EditWorkoutRequest editWorkoutRequest = EditWorkoutRequest.builder()
        .workoutName("new name")
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(Instant.now())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(editWorkoutRequest);

    given(workoutDataService.editWorkout(workoutId, editWorkoutRequest))
        .willThrow(new WorkoutNotFoundException("test message"));

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
        .workoutName("new name")
        .workoutType(WorkoutType.LEGS)
        .performedAtTimestampUtc(now)
        .build();

    Workout expectedWorkout = Workout.builder()
        .id(workoutId)
        .name("new name")
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

    willThrow(new WorkoutNotFoundException("test message"))
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