package com.evans.gymapp.controller;

import com.evans.gymapp.domain.WorkoutType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WorkoutTypeController {

  @CrossOrigin
  @GetMapping("/workoutTypes")
  public WorkoutType[] getWorkoutTypes() {
    return WorkoutType.values();
  }

}
