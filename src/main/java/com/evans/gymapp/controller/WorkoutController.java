package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WorkoutController {

  @NonNull
  private final IWorkoutDataService workoutDataService;

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleResourceNotFoundException() {
    return "workout not found";
  }

  //CrossOrigin required for CORS support. As the React frontend calls locally hosted Spring Boot API
  @CrossOrigin
  @GetMapping("/workouts")
  public List<Workout> getAllWorkouts() {
    return workoutDataService.getAllWorkouts();
  }

  @CrossOrigin
  @GetMapping("/workouts/{workoutId}")
  public ResponseEntity<Workout> getWorkout(@PathVariable long workoutId) {
    return workoutDataService.getWorkoutById(workoutId)
        .map(workout -> ResponseEntity.ok().body(workout))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @GetMapping("/workouts/byName/{workoutName}")
  public ResponseEntity<Workout> getWorkout(@PathVariable String workoutName) {
    return workoutDataService.getWorkoutByName(workoutName)
        .map(workout -> ResponseEntity.ok().body(workout))
        .orElseThrow(ResourceNotFoundException::new);
  }


  //TODO move this to exerciseActivityController
  @CrossOrigin
  @PatchMapping("/workouts/{workoutId}/updateSets")
  public ResponseEntity updateSets(@PathVariable long workoutId, @RequestBody ExerciseActivity exerciseActivity) {
    workoutDataService.updateSets(workoutId, exerciseActivity);

    return ResponseEntity.ok().build();
  }

  @CrossOrigin
  @PostMapping("/workouts/{workoutId}/addExerciseActivity")
  public ResponseEntity addExerciseActivity(@PathVariable long workoutId, @RequestBody long exerciseId) {
    log.info("Add exercise activity request received");
    try {
      ExerciseActivity exerciseActivity = workoutDataService.addExerciseActivity(workoutId, exerciseId);

      return ResponseEntity.ok().body(exerciseActivity);
    } catch (ExerciseNotFoundException | WorkoutNotFoundException e) {
      log.error(e.getMessage());

      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @CrossOrigin
  @DeleteMapping("/workouts/{workoutId}/exerciseActivity/{exerciseActivityId}")
  public ResponseEntity deleteExerciseActivity(@PathVariable long workoutId, @PathVariable long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    try {
      ExerciseActivity exerciseActivity = workoutDataService.deleteExerciseActivity(workoutId, exerciseActivityId);

      return ResponseEntity.ok().body(exerciseActivity);
    } catch (WorkoutNotFoundException | ExerciseActivityNotFoundException e) {
      log.error(e.getMessage());

      //TODO work out what to return for bad request?
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  public class ResourceNotFoundException extends RuntimeException {
  }

}
