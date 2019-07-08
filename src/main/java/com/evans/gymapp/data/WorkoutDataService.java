package com.evans.gymapp.data;

import com.evans.gymapp.data.object.WorkoutDto;
import com.evans.gymapp.repo.WorkoutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkoutDataService {

    @NonNull
    private final WorkoutRepository workoutRepository;

    public Iterable<WorkoutDto> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Optional<WorkoutDto> getWorkout(long workoutId) {
        return workoutRepository.findById(workoutId);
    }

    public void addWorkout(WorkoutDto workout) {
        workoutRepository.save(workout);
    }

}
