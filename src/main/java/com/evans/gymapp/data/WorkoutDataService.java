package com.evans.gymapp.data;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.repo.WorkoutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkoutDataService {

    @NonNull
    private final WorkoutRepository workoutRepository;

    public Iterable<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public void addWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

}
