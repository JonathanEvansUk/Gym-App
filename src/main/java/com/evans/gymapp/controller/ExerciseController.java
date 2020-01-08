package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.request.GetExerciseMetricsResponse;
import com.evans.gymapp.service.IExerciseDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ExerciseController {

  @NonNull
  private final IExerciseDataService exerciseDataService;

  @GetMapping("/exercises")
  public List<Exercise> getExercises() {
    return exerciseDataService.getAllExercises();
  }

  @GetMapping("/exercises/{exerciseId}")
  public Exercise getExerciseById(@PathVariable long exerciseId) throws ExerciseNotFoundException {
    return exerciseDataService.getExerciseById(exerciseId);
  }

  @GetMapping("/exercises/{exerciseId}/metrics")
  public GetExerciseMetricsResponse getExerciseMetricsById(@PathVariable long exerciseId) throws ExerciseNotFoundException {
    return exerciseDataService.getExerciseMetricsById(exerciseId);
  }
}
