package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.service.IExerciseDataService;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.util.converter.ExerciseConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseDataService implements IExerciseDataService {

  @NonNull
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final ExerciseConverter exerciseConverter;

  @Override
  public void addExercise(Exercise exercise) {
    ExerciseEntity exerciseEntity = exerciseConverter.convert(exercise);

    exerciseRepository.save(exerciseEntity);
  }

  @Override
  public void deleteExercise(Exercise exercise) {

  }

  @Override
  public List<Exercise> getAllExercises() {
    return null;
  }

}
