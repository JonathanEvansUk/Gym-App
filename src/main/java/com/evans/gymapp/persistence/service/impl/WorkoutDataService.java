package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.evans.gymapp.util.converter.WorkoutConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutDataService implements IWorkoutDataService {

  @NonNull
  private final WorkoutRepository workoutRepository;

  @NonNull
  //TODO remove this?
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final WorkoutConverter workoutConverter;

  @PostConstruct
  public void initiate() {
    Set<ExerciseActivityEntity> exerciseActivities = new HashSet<>();

    ExerciseEntity exerciseEntity1 = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Bicep curl")
        .build();

    exerciseRepository.save(exerciseEntity1);

    ExerciseSetEntity exerciseSetEntity1 = ExerciseSetEntity.builder()
        .numberOfReps(8)
        .weightKg(10D)
        .status(Status.COMPLETED)
        .build();

    ExerciseSetEntity exerciseSetEntity2 = ExerciseSetEntity.builder()
        .numberOfReps(10)
        .weightKg(12D)
        .status(Status.FAILED)
        .build();

    ExerciseEntity exerciseEntity2 = ExerciseEntity.builder()
        .id(2L)
        .name("Barbell Row")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Barbell Row")
        .build();

    ExerciseSetEntity exerciseSetEntity3 = ExerciseSetEntity.builder()
        .numberOfReps(12)
        .weightKg(40D)
        .status(Status.COMPLETED)
        .build();

    exerciseRepository.save(exerciseEntity2);

    ExerciseActivityEntity exerciseActivityEntity1 = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity1)
        .sets(Arrays.asList(exerciseSetEntity1, exerciseSetEntity2))
        .build();

    ExerciseActivityEntity exerciseActivityEntity2 = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity2)
        .sets(Collections.singletonList(exerciseSetEntity3))
        .build();

    exerciseActivities.add(exerciseActivityEntity1);
    exerciseActivities.add(exerciseActivityEntity2);

    WorkoutEntity workoutEntity1 = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptySet())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    WorkoutEntity workoutEntity2 = WorkoutEntity.builder()
        .name("workout 2")
        .workoutType(WorkoutType.LEGS)
        .exerciseActivities(exerciseActivities)
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    workoutRepository.saveAll(Arrays.asList(workoutEntity1, workoutEntity2));
  }

  @Override
  //TODO maybe return boolean to indicate successful creation?
  public void addWorkout(Workout workout) {
    WorkoutEntity workoutEntity = workoutConverter.convert(workout);

    workoutRepository.save(workoutEntity);
  }

  @Override
  public List<Workout> getAllWorkouts() {
    return workoutRepository.findAll()
        .stream()
        .map(workoutConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Workout> getWorkout(long workoutId) {
    return workoutRepository.findById(workoutId)
        .map(workoutConverter::convert);
  }

  @Override
  public Optional<Workout> getWorkout(String workoutName) {
    return workoutRepository.findByName(workoutName)
        .map(workoutConverter::convert);
  }
}
