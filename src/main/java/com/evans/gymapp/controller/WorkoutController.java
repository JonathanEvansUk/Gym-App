package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
//CrossOrigin required for CORS support. As the React frontend calls locally hosted Spring Boot API
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class WorkoutController {

  @NonNull
  private final IWorkoutDataService workoutDataService;

  @GetMapping("/workouts")
  public List<Workout> getAllWorkouts() {
    return workoutDataService.getAllWorkouts();
  }

  @GetMapping("/workouts/{workoutId}")
  public ResponseEntity<Workout> getWorkoutById(@PathVariable long workoutId) {
    return ResponseEntity.ok()
        .body(workoutDataService.getWorkoutById(workoutId));
  }

  @PostMapping("/workouts/")
  public ResponseEntity<Workout> addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {

    Workout workout = workoutDataService.addWorkout(request);

    return ResponseEntity.ok(workout);
  }

  @PatchMapping("/workouts/{workoutId}")
  public ResponseEntity<Workout> editWorkout(@PathVariable long workoutId, @Valid @RequestBody EditWorkoutRequest request) throws WorkoutNotFoundException {
    Workout workout = workoutDataService.editWorkout(workoutId, request);

    return ResponseEntity.ok(workout);
  }

  @DeleteMapping("/workouts/{workoutId}")
  public ResponseEntity deleteWorkout(@PathVariable long workoutId) throws WorkoutNotFoundException {
    workoutDataService.deleteWorkout(workoutId);

    return ResponseEntity.ok().build();
  }
}
