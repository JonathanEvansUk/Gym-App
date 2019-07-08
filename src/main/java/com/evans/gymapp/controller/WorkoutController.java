package com.evans.gymapp.controller;

import com.evans.gymapp.data.WorkoutDataService;
import com.evans.gymapp.domain.Workout;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkoutController {

    @NonNull
    private final WorkoutDataService workoutDataService;

    @GetMapping("/workouts/")
    public Iterable<Workout> getAllWorkouts() {

        Iterable<Workout> allWorkouts = workoutDataService.getAllWorkouts();


        return allWorkouts;
    }


}
