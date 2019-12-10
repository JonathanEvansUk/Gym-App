package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.service.IExerciseActivityDataService;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity updateSets(@PathVariable long workoutId, @RequestBody ExerciseActivity exerciseActivity) {
    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/workouts/{workoutId}/addExerciseActivity")
  public ResponseEntity addExerciseActivity(@PathVariable long workoutId, @RequestBody long exerciseId) throws WorkoutNotFoundException, ExerciseNotFoundException {
    log.info("Add exercise activity request received");

    ExerciseActivity exerciseActivity = workoutDataService.addExerciseActivity(workoutId, exerciseId);

    return ResponseEntity.ok(exerciseActivity);
  }

  @DeleteMapping("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}")
  public ResponseEntity deleteExerciseActivity(@PathVariable long workoutId, @PathVariable long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {

    // TODO should this return nothing?
    ExerciseActivity exerciseActivity = workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId);

    return ResponseEntity.ok(exerciseActivity);
  }
}
