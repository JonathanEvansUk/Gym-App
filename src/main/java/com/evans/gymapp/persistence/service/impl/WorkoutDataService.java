package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.evans.gymapp.persistence.table.WorkoutEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class WorkoutDataService implements IWorkoutDataService {

  @NonNull
  private final WorkoutRepository workoutRepository;

  @PostConstruct
  public void initiate() {
    WorkoutEntity workoutEntity1 = WorkoutEntity.builder()
        .name("workout 1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivity(Collections.emptyMap())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    WorkoutEntity workoutEntity2 = WorkoutEntity.builder()
        .name("workout 2")
        .workoutType(WorkoutType.LEGS)
        .exerciseActivity(Collections.emptyMap())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    workoutRepository.saveAll(Arrays.asList(workoutEntity1, workoutEntity2));
  }

  @Override
  //TODO maybe return boolean to indicate successful creation?
  public void addWorkout(Workout workout) {
    WorkoutEntity workoutEntity = toWorkoutEntity(workout);

    workoutRepository.save(workoutEntity);
  }

  @Override
  public List<Workout> getAllWorkouts() {
    return StreamSupport.stream(workoutRepository.findAll().spliterator(), false)
        .map(this::toWorkout)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Workout> getWorkout(long workoutId) {
    return workoutRepository.findById(workoutId)
        .map(this::toWorkout);
  }

  @Override
  public Optional<Workout> getWorkout(String workoutName) {
    return workoutRepository.findByName(workoutName)
        .map(this::toWorkout);
  }

  private Workout toWorkout(WorkoutEntity workoutEntity) {
    return Workout.builder()
        .name(workoutEntity.getName())
        .build();
  }

  private WorkoutEntity toWorkoutEntity(Workout workout) {
    return WorkoutEntity.builder()
        .name(workout.getName())
        .build();
  }
}
