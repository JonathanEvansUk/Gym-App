package com.evans.gymapp.controller;

import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.service.IWorkoutTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class WorkoutTypeController {

  @NonNull
  private final IWorkoutTypeService workoutTypeService;

  @GetMapping("/workoutTypes")
  public WorkoutType[] getWorkoutTypes() {
    return workoutTypeService.getWorkoutTypes();
  }
}
