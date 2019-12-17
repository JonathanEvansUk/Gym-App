package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public Workout getWorkoutById(@PathVariable long workoutId) {
    return workoutDataService.getWorkoutById(workoutId);
  }

  @PostMapping("/workouts/")
  public Workout addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
    return workoutDataService.addWorkout(request);
  }

  @PatchMapping("/workouts/{workoutId}")
  public Workout editWorkout(@PathVariable long workoutId, @Valid @RequestBody EditWorkoutRequest request) throws WorkoutNotFoundException {
    return workoutDataService.editWorkout(workoutId, request);
  }

  @DeleteMapping("/workouts/{workoutId}")
  public void deleteWorkout(@PathVariable long workoutId) throws WorkoutNotFoundException {
    workoutDataService.deleteWorkout(workoutId);
  }
}
