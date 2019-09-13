package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.service.IExerciseDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExerciseController {

  @NonNull
  private final IExerciseDataService exerciseDataService;

  @CrossOrigin
  @GetMapping("/exercises")
  public List<Exercise> getExercises() {
    return exerciseDataService.getAllExercises();
  }
}
