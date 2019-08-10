package com.evans.gymapp.controller;

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

  @GetMapping("/workouts/{workoutId}")
  public ResponseEntity<Workout> getWorkout(@PathVariable long workoutId) {
    return workoutDataService.getWorkout(workoutId)
        .map(workout -> ResponseEntity.ok().body(workout))
        .orElseThrow(ResourceNotFoundException::new);
  }

  @GetMapping("/workouts/byName/{workoutName}")
  public ResponseEntity<Workout> getWorkout(@PathVariable String workoutName) {
    return workoutDataService.getWorkout(workoutName)
        .map(workout -> ResponseEntity.ok().body(workout))
        .orElseThrow(ResourceNotFoundException::new);
  }

  public class ResourceNotFoundException extends RuntimeException {
  }

}
