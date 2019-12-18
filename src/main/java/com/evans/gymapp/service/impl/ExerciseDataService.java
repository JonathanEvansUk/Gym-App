package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.service.IExerciseDataService;
import com.evans.gymapp.util.converter.ExerciseConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseDataService implements IExerciseDataService {

  @NonNull
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final ExerciseConverter exerciseConverter;

  @Override
  public List<Exercise> getAllExercises() {
    return exerciseRepository.findAll()
        .stream()
        .map(exerciseConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Exercise getExerciseById(long exerciseId) throws ExerciseNotFoundException {
    ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new ExerciseNotFoundException("exercise not found"));

    return exerciseConverter.convert(exerciseEntity);
  }
}
