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
import java.util.Optional;

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
    Optional<ExerciseActivity> exerciseActivity = workoutDataService.addExerciseActivity(workoutId, exerciseId);

    if (exerciseActivity.isPresent()) {
      log.info("exercise activity is present");
      return ResponseEntity.ok()
          .body(exerciseActivity.get());
    }

    log.info("exercise activity is NOT present");
    //TODO work out what to return
    return ResponseEntity.badRequest().build();
  }

  public class ResourceNotFoundException extends RuntimeException {
  }

}
