package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.ExerciseSet;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseSetEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExerciseActivityConverter {

  @NonNull
  private final ExerciseConverter exerciseConverter;

  @NonNull
  private final ExerciseSetConverter exerciseSetConverter;

  public ExerciseActivity convert(ExerciseActivityEntity exerciseActivityEntity) {
    return ExerciseActivity.builder()
        .id(exerciseActivityEntity.getId())
        .exercise(exerciseConverter.convert(exerciseActivityEntity.getExercise()))
        .sets(convertExerciseSetEntities(exerciseActivityEntity.getSets()))
        .notes(exerciseActivityEntity.getNotes())
        .build();
  }

  private List<ExerciseSet> convertExerciseSetEntities(List<ExerciseSetEntity> exerciseSetEntities) {
    return exerciseSetEntities.stream()
        .map(exerciseSetConverter::convert)
        .collect(Collectors.toList());
  }

  public ExerciseActivityEntity convert(ExerciseActivity exerciseActivity) {
    return ExerciseActivityEntity.builder()
        .id(exerciseActivity.getId())
        .exercise(exerciseConverter.convert(exerciseActivity.getExercise()))
        .sets(convertExerciseSets(exerciseActivity.getSets()))
        .notes(exerciseActivity.getNotes())
        .build();
  }

  private List<ExerciseSetEntity> convertExerciseSets(List<ExerciseSet> exerciseSets) {
    return exerciseSets.stream()
        .map(exerciseSetConverter::convert)
        .collect(Collectors.toList());
  }
}
