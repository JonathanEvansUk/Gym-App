package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkoutConverter {

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;

  public Workout convert(WorkoutEntity workoutEntity) {
    return Workout.builder()
        .id(workoutEntity.getId())
        .name(workoutEntity.getName())
        .workoutType(workoutEntity.getWorkoutType())
        .exerciseActivities(convertExerciseEntityActivity(workoutEntity.getExerciseActivities()))
        .build();
  }

  private Set<ExerciseActivity> convertExerciseEntityActivity(Set<ExerciseActivityEntity> exerciseActivities) {
    return exerciseActivities.stream()
        .map(exerciseActivityConverter::convert)
        .collect(Collectors.toSet());
  }

  public WorkoutEntity convert(Workout workout) {
    return WorkoutEntity.builder()
        .id(workout.getId())
        .name(workout.getName())
        .workoutType(workout.getWorkoutType())
        .exerciseActivities(convertExerciseActivity(workout.getExerciseActivities()))
        .build();
  }

  private Set<ExerciseActivityEntity> convertExerciseActivity(Set<ExerciseActivity> exerciseActivities) {
    return exerciseActivities.stream()
        .map(exerciseActivityConverter::convert)
        .collect(Collectors.toSet());
  }
}
