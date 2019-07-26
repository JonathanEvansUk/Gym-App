package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.table.ExerciseEntity;
import com.evans.gymapp.persistence.table.WorkoutEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkoutConverter {

  @NonNull
  private final ExerciseConverter exerciseConverter;

  public Workout convert(WorkoutEntity workoutEntity) {
    return Workout.builder()
        .name(workoutEntity.getName())
        .workoutType(workoutEntity.getWorkoutType())
        .exerciseActivity(convertExerciseEntityActivity(workoutEntity.getExerciseActivity()))
        .build();
  }

  private Map<Exercise, ExerciseActivity> convertExerciseEntityActivity(Map<ExerciseEntity, ExerciseActivity> exerciseActivity) {
    return exerciseActivity.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> exerciseConverter.convert(entry.getKey()),
            Map.Entry::getValue
        ));
  }

  public WorkoutEntity convert(Workout workout) {
    return WorkoutEntity.builder()
        .name(workout.getName())
        .workoutType(workout.getWorkoutType())
        .exerciseActivity(convertExerciseActivity(workout.getExerciseActivity()))
        .build();
  }

  private Map<ExerciseEntity, ExerciseActivity> convertExerciseActivity(Map<Exercise, ExerciseActivity> exerciseActivity) {
    return exerciseActivity.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> exerciseConverter.convert(entry.getKey()),
            Map.Entry::getValue
        ));
  }
}
