package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WorkoutController {

  @NonNull
  private final IWorkoutDataService workoutDataService;

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException() {
    return "workout not found";
  }


  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity handleInvalidFormatException(InvalidFormatException exception) {
    return ResponseEntity.badRequest().body(exception);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    Map<String, String> errors = exception.getBindingResult().getAllErrors().stream()
        .filter(FieldError.class::isInstance)
        .map(FieldError.class::cast)
        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

    return ResponseEntity.badRequest().body(errors);
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

  @CrossOrigin
  @PostMapping("/workouts/")
  public ResponseEntity<Workout> addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {

    Workout workout = workoutDataService.addWorkout(request);

    return ResponseEntity.ok(workout);
  }

  @CrossOrigin
  @PatchMapping("/workouts/{workoutId}")
  public ResponseEntity<Workout> editWorkout(@PathVariable long workoutId, @Valid @RequestBody EditWorkoutRequest request) throws WorkoutNotFoundException {
    Workout workout = workoutDataService.editWorkout(workoutId, request);

    return ResponseEntity.ok(workout);
  }

  @CrossOrigin
  @DeleteMapping("/workouts/{workoutId}")
  public ResponseEntity deleteWorkout(@PathVariable long workoutId) throws WorkoutNotFoundException {
    workoutDataService.deleteWorkout(workoutId);

    return ResponseEntity.ok().build();
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

      //TODO maybe do not return deleted exerciseActivity?
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
