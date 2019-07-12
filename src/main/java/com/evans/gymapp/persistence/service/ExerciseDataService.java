package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.table.ExerciseEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseDataService implements IExerciseDataService {

  @NonNull
  private final ExerciseRepository exerciseRepository;

  @Override
  public void addExercise(Exercise exercise) {
    ExerciseEntity exerciseEntity = toExerciseEntity(exercise);

    exerciseRepository.save(exerciseEntity);
  }

  @Override
  public void deleteExercise(Exercise exercise) {

  }

  @Override
  public List<Exercise> getAllExercises() {
    return null;
  }

  //TODO pull this out to common transformer / converter class - also found in WorkoutDataService
  private ExerciseEntity toExerciseEntity(Exercise exercise) {
    return ExerciseEntity.builder()
        .name(exercise.getName())
        .build();
  }
}
