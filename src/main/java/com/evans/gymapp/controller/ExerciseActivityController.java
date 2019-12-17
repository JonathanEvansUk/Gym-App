package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.service.IWorkoutDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ExerciseActivityController {

  @NonNull
  private final IExerciseActivityDataService exerciseActivityDataService;

  @NonNull
  private final IWorkoutDataService workoutDataService;

  //TODO update this endpoint
  @PatchMapping("/workouts/{workoutId}/updateSets")
  public void updateSets(@PathVariable long workoutId, @RequestBody ExerciseActivity exerciseActivity) {
    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);
  }

  @PostMapping("/workouts/{workoutId}/addExerciseActivity")
  public ExerciseActivity addExerciseActivity(@PathVariable long workoutId, @RequestBody long exerciseId) throws WorkoutNotFoundException, ExerciseNotFoundException {
    log.info("Add exercise activity request received");

    return workoutDataService.addExerciseActivity(workoutId, exerciseId);
  }

  @DeleteMapping("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}")
  public ExerciseActivity deleteExerciseActivity(@PathVariable long workoutId, @PathVariable long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {

    // TODO should this return nothing?
    return workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId);
  }
}
