package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.service.IExerciseDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private IExerciseDataService exerciseDataService;

  @Test
  public void getExercises() throws Exception {

    given(exerciseDataService.getAllExercises())
        .willReturn(Collections.emptyList());

    MockHttpServletResponse response = mockMvc.perform(get("/exercises"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    List<Exercise> exercises = Arrays.asList(objectMapper.readValue(response.getContentAsString(), Exercise[].class));

    assertNotNull(exercises);
    assertTrue(exercises.isEmpty());

    verify(exerciseDataService)
        .getAllExercises();
  }

  @Test
  public void getExerciseById_exerciseNotFound() throws Exception {

    long exerciseId = 1L;

    given(exerciseDataService.getExerciseById(exerciseId))
        .willThrow(ExerciseNotFoundException.class);

    mockMvc.perform(get("/exercises/{exerciseId}", exerciseId))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    verify(exerciseDataService).getExerciseById(exerciseId);
  }

  @Test
  public void getExerciseById() throws Exception {

    long exerciseId = 1L;

    Exercise expectedExercise = Exercise.builder()
        .id(exerciseId)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Information")
        .build();

    given(exerciseDataService.getExerciseById(exerciseId))
        .willReturn(expectedExercise);

    MockHttpServletResponse response = mockMvc.perform(get("/exercises/{exerciseId}", exerciseId))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    Exercise exercise = objectMapper.readValue(response.getContentAsString(), Exercise.class);

    assertEquals(expectedExercise, exercise);

    verify(exerciseDataService).getExerciseById(exerciseId);
  }
}