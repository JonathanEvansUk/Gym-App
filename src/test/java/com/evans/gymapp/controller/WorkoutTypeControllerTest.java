package com.evans.gymapp.controller;

import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.service.IWorkoutTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutTypeController.class)
public class WorkoutTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private IWorkoutTypeService workoutTypeService;

  @Test
  public void getWorkoutTypes() throws Exception {

    given(workoutTypeService.getWorkoutTypes())
        .willReturn(WorkoutType.values());

    MockHttpServletResponse response = mockMvc.perform(
        get("/workoutTypes")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    WorkoutType[] workoutTypes = objectMapper.readValue(response.getContentAsString(), WorkoutType[].class);

    assertArrayEquals(WorkoutType.values(), workoutTypes);

    verify(workoutTypeService).getWorkoutTypes();
  }
}