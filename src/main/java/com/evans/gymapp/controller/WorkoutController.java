package com.evans.gymapp.controller;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.service.WorkoutDataService;
import com.evans.gymapp.persistence.table.WorkoutEntity;
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
    private final WorkoutDataService workoutDataService;

    @GetMapping("/workouts")
    public List<Workout> getAllWorkouts() {
        List<Workout> allWorkouts = workoutDataService.getAllWorkouts();

        return allWorkouts;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "workout not found";
    }

    @GetMapping("/workouts/{workoutId}")
    public ResponseEntity<WorkoutEntity> getWorkout(@PathVariable long workoutId) {
        Optional<WorkoutEntity> workoutDto = workoutDataService.getWorkout(workoutId);

        return workoutDto
                .map(workout -> ResponseEntity.ok().body(workout))
                .orElseThrow(ResourceNotFoundException::new);


        // TODO workout if this is best way to handle no workout being found.
        // will eventually want to have some kind of fallback page
//        return workoutDto
//                .map(workout -> ResponseEntity.ok().body(workout))
//                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public class ResourceNotFoundException extends RuntimeException {
    }

}
