package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.table.WorkoutTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class WorkoutDataService {

    @NonNull
    private final WorkoutRepository workoutRepository;

    public List<Workout> getAllWorkouts() {
        return StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                .map(this::toWorkout)
                .collect(Collectors.toList());
    }

    public Optional<WorkoutTable> getWorkout(long workoutId) {
        return workoutRepository.findById(workoutId);
    }

    public void addWorkout(WorkoutTable workout) {
        workoutRepository.save(workout);
    }

    private Workout toWorkout(WorkoutTable workoutTable) {
        return Workout.builder()
                .name(workoutTable.getName())
                .build();
    }

}
