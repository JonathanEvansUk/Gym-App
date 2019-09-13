package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.persistence.service.IExerciseActivityDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExerciseActivityController {

  @NonNull
  private final IExerciseActivityDataService exerciseActivityDataService;

  @CrossOrigin
  @PutMapping("/exerciseActivity")
  public ResponseEntity addExerciseActivity(@RequestBody ExerciseActivity exerciseActivity) {
    exerciseActivityDataService.addExerciseActivity(exerciseActivity);

    return ResponseEntity.ok().build();
  }

}
