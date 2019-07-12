package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.table.ExerciseEntity;
import com.evans.gymapp.persistence.table.WorkoutEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class WorkoutDataService {

    @NonNull
    private final WorkoutRepository workoutRepository;

    @PostConstruct
    public void initiate() {
        ExerciseEntity exerciseEntity1 = new ExerciseEntity("exercise 1");
        ExerciseEntity exerciseEntity2 = new ExerciseEntity("exercise 2");

        WorkoutEntity workoutEntity1 = new WorkoutEntity("workout 1", Collections.singletonList(exerciseEntity1));
        WorkoutEntity workoutEntity2 = new WorkoutEntity("workout 2", Collections.singletonList(exerciseEntity2));

        workoutRepository.saveAll(Arrays.asList(workoutEntity1, workoutEntity2));
    }

    public List<Workout> getAllWorkouts() {
        return StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
                .map(this::toWorkout)
                .collect(Collectors.toList());
    }

    public Optional<WorkoutEntity> getWorkout(long workoutId) {
        return workoutRepository.findById(workoutId);
    }

    public void addWorkout(WorkoutEntity workout) {
        workoutRepository.save(workout);
    }

    private Workout toWorkout(WorkoutEntity workoutEntity) {
        return Workout.builder()
                .name(workoutEntity.getName())
                .exercises(convertExerciseEntities(workoutEntity.getExercises()))
                .build();
    }

    private List<Exercise> convertExerciseEntities(List<ExerciseEntity> exercises) {
        return exercises.stream()
                .map(this::toExercise)
                .collect(Collectors.toList());
    }

    private Exercise toExercise(ExerciseEntity exerciseEntity) {
        return Exercise.builder()
                .name(exerciseEntity.getName())
                .build();
    }
}
