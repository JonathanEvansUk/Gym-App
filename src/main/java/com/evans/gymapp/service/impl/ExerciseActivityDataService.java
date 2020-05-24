package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseSetConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseActivityDataService implements IExerciseActivityDataService {

  @NonNull
  private final ExerciseActivityRepository exerciseActivityRepository;

  @NonNull
  private final ExerciseSetConverter exerciseSetConverter;

  // TODO think about what we send in request. Only need exerciseActivityId and list of sets
  @Override
  public void updateSets(long workoutId, ExerciseActivity exerciseActivity) throws ExerciseActivityNotFoundException {
    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityRepository.findById(exerciseActivity.getId())
        .orElseThrow(() -> new ExerciseActivityNotFoundException("Exercise Activity not found"));

    List<ExerciseSetEntity> sets = exerciseActivity.getSets()
        .stream()
        .map(exerciseSetConverter::convert)
        .collect(Collectors.toList());

    ExerciseActivityEntity updatedExerciseActivity = exerciseActivityEntity.toBuilder()
        .sets(sets)
        .build();

    updatedExerciseActivity.getSets()
        .forEach(set -> set.setExerciseActivity(updatedExerciseActivity));

    exerciseActivityRepository.save(updatedExerciseActivity);
  }
}
