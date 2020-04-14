package com.evans.gymapp.controller;

import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.service.IWorkoutTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.evans.gymapp.util.EndpointConstants.WORKOUT_TYPES;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(WORKOUT_TYPES)
@RequiredArgsConstructor
public class WorkoutTypeController {

  @NonNull
  private final IWorkoutTypeService workoutTypeService;

  // TODO maybe this should be removed and replaced with consolidated calls
  @GetMapping
  public WorkoutType[] getWorkoutTypes() {
    return workoutTypeService.getWorkoutTypes();
  }
}
