package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.evans.gymapp.util.EndpointConstants.WORKOUTS;
import static com.evans.gymapp.util.EndpointConstants.WORKOUT_ID;

@Slf4j
//CrossOrigin required for CORS support. As the React frontend calls locally hosted Spring Boot API
@CrossOrigin
@RestController
@RequestMapping(WORKOUTS)
@RequiredArgsConstructor
public class WorkoutController {

  @NonNull
  private final IWorkoutDataService workoutDataService;

  @GetMapping
  public List<Workout> getAllWorkouts() {
    log.info("Get all workouts request received");

    return workoutDataService.getAllWorkouts();
  }

  @GetMapping(WORKOUT_ID)
  public Workout getWorkoutById(@PathVariable long workoutId) throws WorkoutNotFoundException {
    log.info("Get workout by id request received: workoutId: {}", workoutId);

    return workoutDataService.getWorkoutById(workoutId);
  }

  @PostMapping
  public Workout addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
    log.info("Add workout request received: {}", request);

    return workoutDataService.addWorkout(request);
  }

  @PatchMapping(WORKOUT_ID)
  public Workout editWorkout(@PathVariable long workoutId, @Valid @RequestBody EditWorkoutRequest request) throws WorkoutNotFoundException {
    log.info("Edit workout request received: workoutId: {}, {}", workoutId, request);

    return workoutDataService.editWorkout(workoutId, request);
  }

  @DeleteMapping(WORKOUT_ID)
  public void deleteWorkout(@PathVariable long workoutId) throws WorkoutNotFoundException {
    log.info("Delete workout request received: workoutId: {}", workoutId);

    workoutDataService.deleteWorkout(workoutId);
  }
}
