package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseActivityDataService implements IExerciseActivityDataService {

  @NonNull
  private final ExerciseActivityRepository exerciseActivityRepository;

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;


  @Override
  public void updateSets(long workoutId, ExerciseActivity exerciseActivity) {
    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);
    exerciseActivityRepository.save(exerciseActivityEntity);
  }
}
