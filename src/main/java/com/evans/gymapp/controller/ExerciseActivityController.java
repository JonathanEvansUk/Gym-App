package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IExerciseActivityDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ExerciseActivityController {

  @NonNull
  private final IExerciseActivityDataService exerciseActivityDataService;

  //TODO update this endpoint, and add constants
  @PatchMapping("/workouts/{workoutId}/updateSets")
  public void updateSets(@PathVariable long workoutId, @Valid @RequestBody ExerciseActivity exerciseActivity) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);
  }

  @PostMapping("/workouts/{workoutId}/addExerciseActivity")
  public ExerciseActivity addExerciseActivity(@PathVariable long workoutId, @RequestBody long exerciseId) throws WorkoutNotFoundException, ExerciseNotFoundException {
    log.info("Add exercise activity request received");

    return exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId);
  }

  @DeleteMapping("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}")
  public ExerciseActivity deleteExerciseActivity(@PathVariable long workoutId, @PathVariable long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {

    // TODO should this return nothing?
    return exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId);
  }
}
