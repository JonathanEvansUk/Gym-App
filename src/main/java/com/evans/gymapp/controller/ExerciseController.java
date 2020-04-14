package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.service.IExerciseDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.evans.gymapp.util.EndpointConstants.EXERCISES;
import static com.evans.gymapp.util.EndpointConstants.EXERCISE_ID;

@CrossOrigin
@RestController
@RequestMapping(EXERCISES)
@RequiredArgsConstructor
public class ExerciseController {

  @NonNull
  private final IExerciseDataService exerciseDataService;

  @GetMapping
  public List<Exercise> getExercises() {
    return exerciseDataService.getAllExercises();
  }

  @GetMapping(EXERCISE_ID)
  public Exercise getExerciseById(@PathVariable long exerciseId) throws ExerciseNotFoundException {
    return exerciseDataService.getExerciseById(exerciseId);
  }
}
