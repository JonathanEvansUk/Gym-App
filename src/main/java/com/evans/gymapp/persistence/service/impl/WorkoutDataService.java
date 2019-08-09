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
    Map<ExerciseEntity, ExerciseActivityEntity> exerciseActivity = new HashMap<>();

    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Bicep curl")
        .build();

    exerciseRepository.save(exerciseEntity);

    ExerciseSetEntity exerciseSetEntity = ExerciseSetEntity.builder()
        .numberOfReps(8)
        .weightKg(10D)
        .status(Status.COMPLETED)
        .build();

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .sets(Collections.singletonList(exerciseSetEntity))
        .build();

    exerciseActivity.put(exerciseEntity, exerciseActivityEntity);

    WorkoutEntity workoutEntity1 = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivity(Collections.emptyMap())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    WorkoutEntity workoutEntity2 = WorkoutEntity.builder()
        .name("workout 2")
        .workoutType(WorkoutType.LEGS)
        .exerciseActivity(exerciseActivity)
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
        //TODO remove peek
        .peek(i -> log.info(i.toString()))
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
