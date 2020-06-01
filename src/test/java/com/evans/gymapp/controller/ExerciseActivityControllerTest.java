package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseActivityController.class)
public class ExerciseActivityControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private IExerciseActivityDataService exerciseActivityDataService;

  private static final String TRICEP_PUSHDOWN = "Tricep Pushdown";

  @Test
  public void updateSets_exerciseActivityNotFound() throws Exception {
    long workoutId = 1L;

    Exercise tricepPushdown = createExercise();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .exercise(tricepPushdown)
        .sets(Collections.emptyList())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(exerciseActivity);

    doThrow(ExerciseActivityNotFoundException.class)
        .when(exerciseActivityDataService)
        .updateSets(workoutId, exerciseActivity);

    mockMvc.perform(patch("/workouts/{workoutId}/updateSets", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    verify(exerciseActivityDataService).updateSets(workoutId, exerciseActivity);
  }

  @Test
  public void updateSets() throws Exception {
    long workoutId = 1L;

    Exercise tricepPushdown = createExercise();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .exercise(tricepPushdown)
        .sets(Collections.emptyList())
        .build();

    String jsonRequest = objectMapper.writeValueAsString(exerciseActivity);

    mockMvc.perform(patch("/workouts/{workoutId}/updateSets", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isOk())
        .andReturn();

    verify(exerciseActivityDataService).updateSets(workoutId, exerciseActivity);
  }

  @Test
  public void addExerciseActivity_workoutNotFound() throws Exception {
    long workoutId = 1L;
    long exerciseId = 1L;

    given(exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId))
        .willThrow(WorkoutNotFoundException.class);

    String jsonRequest = objectMapper.writeValueAsString(exerciseId);

    mockMvc.perform(post("/workouts/{workoutId}/addExerciseActivity", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isNotFound())
        .andReturn();

    verify(exerciseActivityDataService).addExerciseActivity(workoutId, exerciseId);
  }

  @Test
  public void addExerciseActivity_exerciseNotFound() throws Exception {

    long workoutId = 1L;
    long exerciseId = 1L;

    given(exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId))
        .willThrow(ExerciseNotFoundException.class);

    String jsonRequest = objectMapper.writeValueAsString(exerciseId);

    mockMvc.perform(post("/workouts/{workoutId}/addExerciseActivity", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isNotFound())
        .andReturn();

    verify(exerciseActivityDataService).addExerciseActivity(workoutId, exerciseId);
  }

  @Test
  public void addExerciseActivity() throws Exception {
    long workoutId = 1L;
    long exerciseId = 1L;

    ExerciseActivity expectedExerciseActivity = ExerciseActivity.builder()
        .exercise(createExercise())
        .sets(Collections.emptyList())
        .notes("test notes")
        .build();

    given(exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId))
        .willReturn(expectedExerciseActivity);

    String jsonRequest = objectMapper.writeValueAsString(exerciseId);

    MockHttpServletResponse response = mockMvc.perform(post("/workouts/{workoutId}/addExerciseActivity", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    ExerciseActivity exerciseActivity = objectMapper.readValue(response.getContentAsString(), ExerciseActivity.class);

    assertEquals(expectedExerciseActivity, exerciseActivity);

    verify(exerciseActivityDataService).addExerciseActivity(workoutId, exerciseId);
  }

  // TODO may need to update tests to see what exception details include
  @Test
  public void deleteExerciseActivity_exerciseActivityNotFound() throws Exception {
    long workoutId = 1L;
    long exerciseActivityId = 1L;

    given(exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId))
        .willThrow(ExerciseActivityNotFoundException.class);

    mockMvc.perform(
        delete("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}", workoutId, exerciseActivityId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andReturn();

    verify(exerciseActivityDataService).deleteExerciseActivity(workoutId, exerciseActivityId);
  }

  @Test
  public void deleteExerciseActivity() throws Exception {

    long workoutId = 1L;
    long exerciseActivityId = 1L;

    ExerciseActivity expectedExerciseActivity = ExerciseActivity.builder()
        .id(exerciseActivityId)
        .sets(Collections.emptyList())
        .notes("test")
        .build();

    given(exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId))
        .willReturn(expectedExerciseActivity);

    MockHttpServletResponse response = mockMvc.perform(
        delete("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}", workoutId, exerciseActivityId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    ExerciseActivity exerciseActivity = objectMapper.readValue(response.getContentAsString(), ExerciseActivity.class);

    assertEquals(expectedExerciseActivity, exerciseActivity);

    verify(exerciseActivityDataService).deleteExerciseActivity(workoutId, exerciseActivityId);
  }

  private Exercise createExercise() {
    return Exercise.builder()
        .name(TRICEP_PUSHDOWN)
        .muscleGroup(MuscleGroup.TRICEP)
        .build();
  }
}