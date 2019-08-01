package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.domain.Routine;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.RoutineEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoutineConverter {

  @NonNull
  private final ExerciseConverter exerciseConverter;

  public Routine convert(RoutineEntity routineEntity) {
    return Routine.builder()
        .name(routineEntity.getName())
        .exercises(convertExerciseEntities(routineEntity.getExercises()))
        .lastPerformedTimestampUtc(routineEntity.getLastPerformedTimestampUtc())
        .notes(routineEntity.getNotes())
        .build();
  }

  private List<Exercise> convertExerciseEntities(List<ExerciseEntity> exerciseEntities) {
    return exerciseEntities.stream()
        .map(exerciseConverter::convert)
        .collect(Collectors.toList());
  }

  public RoutineEntity convert(Routine routine) {
    return RoutineEntity.builder()
        .name(routine.getName())
        .exercises(convertExercises(routine.getExercises()))
        .lastPerformedTimestampUtc(routine.getLastPerformedTimestampUtc())
        .notes(routine.getNotes())
        .build();
  }

  private List<ExerciseEntity> convertExercises(List<Exercise> exercises) {
    return exercises.stream()
        .map(exerciseConverter::convert)
        .collect(Collectors.toList());
  }
}
