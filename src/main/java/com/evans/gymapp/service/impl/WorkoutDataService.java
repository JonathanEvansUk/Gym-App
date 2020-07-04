package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.util.converter.WorkoutConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutDataService implements IWorkoutDataService {

  @NonNull
  private final WorkoutRepository workoutRepository;

  @NonNull
  private final WorkoutConverter workoutConverter;

  @Override
  //TODO maybe return boolean to indicate successful creation?
  public Workout addWorkout(CreateWorkoutRequest request) {
    WorkoutEntity workoutEntity = createNewWorkoutEntity(request);

    WorkoutEntity savedWorkoutEntity = workoutRepository.save(workoutEntity);

    return workoutConverter.convert(savedWorkoutEntity);
  }

  @Override
  public Workout editWorkout(long workoutId, EditWorkoutRequest request) throws WorkoutNotFoundException {
    WorkoutEntity workoutEntity = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException(workoutId));

    // TODO should this be in a converter?
    WorkoutEntity updatedWorkoutEntity = workoutEntity.toBuilder()
        .workoutType(request.getWorkoutType())
        .performedAtTimestampUtc(request.getPerformedAtTimestampUtc())
        .build();

    WorkoutEntity savedWorkoutEntity = workoutRepository.save(updatedWorkoutEntity);

    return workoutConverter.convert(savedWorkoutEntity);
  }

  @Override
  public void deleteWorkout(long workoutId) throws WorkoutNotFoundException {
    WorkoutEntity workoutToDelete = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException(workoutId));

    workoutRepository.delete(workoutToDelete);
  }

  private WorkoutEntity createNewWorkoutEntity(CreateWorkoutRequest request) {
    return WorkoutEntity.builder()
        .workoutType(request.getWorkoutType())
        .performedAtTimestampUtc(request.getPerformedAtTimestampUtc())
        .exerciseActivities(Collections.emptyList())
        .build();
  }

  @Override
  public List<Workout> getAllWorkouts() {
    return workoutRepository.findAll()
        .stream()
        .map(workoutConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Workout getWorkoutById(long workoutId) throws WorkoutNotFoundException {
    return workoutRepository.findById(workoutId)
        .map(workoutConverter::convert)
        .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
  }
}
