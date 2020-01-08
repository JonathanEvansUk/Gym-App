package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.GetExerciseMetricsResponse;
import com.evans.gymapp.service.IExerciseDataService;
import com.evans.gymapp.util.converter.ExerciseConverter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseDataService implements IExerciseDataService {

  @NonNull
  private final ExerciseRepository exerciseRepository;

  // TODO this should not be used. We will invert the relationship of WorkoutEntity to ExerciseEntity.
  // Hence we will be able to fetch all workouts from the exercise
  @NonNull
  private final WorkoutRepository workoutRepository;

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

  @Override
  public GetExerciseMetricsResponse getExerciseMetricsById(long exerciseId) throws ExerciseNotFoundException {
    ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new EntityNotFoundException("exercise not found"));

    List<WorkoutEntity> allWorkouts = workoutRepository.findAll();

    List<WorkoutInformation> workoutInformations = allWorkouts.stream()
        .filter(workout -> workoutContainsExercise(workout, exerciseEntity))
        .map(workout -> new WorkoutInformation(workout.getId(), workout.getPerformedAtTimestampUtc(),
            getHighestWeight(workout.getExerciseActivities().stream()
                .filter(exerciseActivityEntity -> exerciseEntity.equals(exerciseActivityEntity.getExercise()))
                .collect(Collectors.toList()))))
        .collect(Collectors.toList());


//    List<ExerciseActivityEntity> exerciseActivities = allWorkouts.stream()
//        .map(WorkoutEntity::getExerciseActivities)
//        .flatMap(Collection::stream)
//        .filter(exerciseActivityEntity -> exerciseEntity.equals(exerciseActivityEntity.getExercise()))
//        .collect(Collectors.toList());
//
//    double completedSets = exerciseActivities.stream()
//        .map(ExerciseActivityEntity::getSets)
//        .flatMap(Collection::stream)
//        .map(ExerciseSetEntity::getStatus)
//        .filter(status -> status == COMPLETED)
//        .count();
//
//    // TODO this is wrong need to divide by total number of sets
//    double successRate = completedSets / exerciseActivities.size() * 100D;

    return GetExerciseMetricsResponse.builder()
//        .numberOfTimesPerformed(exerciseActivities.size())
//        .successRate(successRate)
//        .workouts()
        .workoutInformations(workoutInformations)
        .build();
  }

  private boolean workoutContainsExercise(WorkoutEntity workoutEntity, ExerciseEntity exerciseEntity) {
    return workoutEntity.getExerciseActivities()
        .stream()
        .anyMatch(exerciseActivityEntity -> exerciseEntity.equals(exerciseActivityEntity.getExercise()));
  }

  private Double getHighestWeight(List<ExerciseActivityEntity> exerciseActivities) {
    // TODO the orElse should probably not be here
    return exerciseActivities.stream()
        .map(ExerciseActivityEntity::getSets)
        .flatMap(Collection::stream)
        .filter(set -> set instanceof WeightedSetEntity)
        .map(set -> (WeightedSetEntity) set)
        .map(WeightedSetEntity::getWeightKg)
        .reduce(Double::max)
        .orElse(0D);
  }

  @Getter
  @RequiredArgsConstructor
  public class WorkoutInformation {

    @NonNull
    private final long id;

    @NonNull
    private final Instant performedAtTimestamp;

    @NonNull
    private final Double highestWeightSet;
  }
}
