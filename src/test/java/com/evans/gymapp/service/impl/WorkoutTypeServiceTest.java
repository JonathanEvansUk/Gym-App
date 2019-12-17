package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.service.IWorkoutTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(MockitoExtension.class)
public class WorkoutTypeServiceTest {

  private final IWorkoutTypeService workoutTypeService = new WorkoutTypeService();

  @Test
  public void getWorkoutTypes() {
    assertArrayEquals(WorkoutType.values(), workoutTypeService.getWorkoutTypes());
  }
}