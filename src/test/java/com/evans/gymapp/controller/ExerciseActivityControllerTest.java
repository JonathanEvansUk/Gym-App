package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

  private static final TypeReference<Map<String, String>> MAP_STRING_STRING_TYPE = new TypeReference<Map<String, String>>() {
  };

  private static final String MUST_NOT_BE_NULL = "must not be null";
  private static final String MUST_NOT_BE_BLANK = "must not be blank";

  @Test
  public void updateSets_invalidRequest_nullSets() throws Exception {
    long workoutId = 1L;

    Exercise tricepPushdown = createExercise();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .exercise(tricepPushdown)
        .sets(null)
        .build();

    String jsonRequest = objectMapper.writeValueAsString(exerciseActivity);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}/updateSets", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    Map<String, String> errors = objectMapper.readValue(response.getContentAsString(), MAP_STRING_STRING_TYPE);

    assertEquals(1, errors.size());
    assertThat(errors, hasEntry("sets", MUST_NOT_BE_NULL));

    verifyZeroInteractions(exerciseActivityDataService);
  }

  @Test
  public void updateSets_invalidRequest_weightedSet() throws Exception {
    long workoutId = 1L;

    Exercise tricepPushdown = createExercise();

    WeightedSet weightedSet = WeightedSet.builder()
        .numberOfReps(null)
        .weightKg(null)
        .status(null)
        .build();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .exercise(tricepPushdown)
        .sets(Collections.singletonList(weightedSet))
        .build();

    String jsonRequest = objectMapper.writeValueAsString(exerciseActivity);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}/updateSets", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    Map<String, String> errors = objectMapper.readValue(response.getContentAsString(), MAP_STRING_STRING_TYPE);

    assertEquals(3, errors.size());
    assertThat(errors, hasEntry("sets[0].numberOfReps", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("sets[0].weightKg", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("sets[0].status", MUST_NOT_BE_NULL));

    verifyZeroInteractions(exerciseActivityDataService);
  }

  private static Stream<String> blankStrings() {
    return Stream.of(null, "", " ");
  }

  @ParameterizedTest(name = "{index} Invalid Request when weight is: \"{0}\"")
  @MethodSource("blankStrings")
  public void updateSets_invalidRequest_nonWeightedSet(String weight) throws Exception {
    long workoutId = 1L;

    Exercise tricepPushdown = createExercise();

    NonWeightedSet nonWeightedSet = NonWeightedSet.builder()
        .numberOfReps(null)
        .weight(weight)
        .status(null)
        .build();

    ExerciseActivity exerciseActivity = ExerciseActivity.builder()
        .exercise(tricepPushdown)
        .sets(Collections.singletonList(nonWeightedSet))
        .build();

    String jsonRequest = objectMapper.writeValueAsString(exerciseActivity);

    MockHttpServletResponse response = mockMvc.perform(patch("/workouts/{workoutId}/updateSets", workoutId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse();

    Map<String, String> errors = objectMapper.readValue(response.getContentAsString(), MAP_STRING_STRING_TYPE);

    assertEquals(3, errors.size());
    assertThat(errors, hasEntry("sets[0].numberOfReps", MUST_NOT_BE_NULL));
    assertThat(errors, hasEntry("sets[0].weight", MUST_NOT_BE_BLANK));
    assertThat(errors, hasEntry("sets[0].status", MUST_NOT_BE_NULL));
  }

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