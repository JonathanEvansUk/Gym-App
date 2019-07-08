package com.evans.gymapp.controller;

import com.evans.gymapp.data.WorkoutDataService;
import com.evans.gymapp.data.object.WorkoutDto;
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
    public Iterable<WorkoutDto> getAllWorkouts() {

        Iterable<WorkoutDto> allWorkouts = workoutDataService.getAllWorkouts();

        return allWorkouts;
    }

    @GetMapping("/add/")
    public void addSomeWorkouts() {

        WorkoutDto workout1 = new WorkoutDto("leg");
        WorkoutDto workout2 = new WorkoutDto("chest");
        WorkoutDto workout3 = new WorkoutDto("back");

        workoutDataService.addWorkout(workout1);
        workoutDataService.addWorkout(workout2);
        workoutDataService.addWorkout(workout3);
    }

}
