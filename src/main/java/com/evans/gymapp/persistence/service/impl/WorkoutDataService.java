package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.controller.*;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import com.evans.gymapp.util.converter.WorkoutConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
  private final ExerciseActivityRepository exerciseActivityRepository;

  @NonNull
  private final WorkoutConverter workoutConverter;

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;

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
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    WorkoutEntity updatedWorkoutEntity = workoutEntity.toBuilder()
        .name(request.getWorkoutName())
        .workoutType(request.getWorkoutType())
        .performedAtTimestampUtc(request.getPerformedAtTimestampUtc())
        .build();

    WorkoutEntity savedWorkoutEntity = workoutRepository.save(updatedWorkoutEntity);

    return workoutConverter.convert(savedWorkoutEntity);
  }

  @Override
  public void deleteWorkout(long workoutId) throws WorkoutNotFoundException {
    WorkoutEntity workoutToDelete = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    workoutRepository.delete(workoutToDelete);
  }

  private WorkoutEntity createNewWorkoutEntity(CreateWorkoutRequest request) {
    return WorkoutEntity.builder()
        .name(request.getWorkoutName())
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
  public Optional<Workout> getWorkoutById(long workoutId) {
    return workoutRepository.findById(workoutId)
        .map(workoutConverter::convert);
  }

  @Override
  public Optional<Workout> getWorkoutByName(String workoutName) {
    return workoutRepository.findByName(workoutName)
        .map(workoutConverter::convert);
  }

  @Override
  public void updateSets(long workoutId, ExerciseActivity exerciseActivity) {
    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);
    exerciseActivityRepository.save(exerciseActivityEntity);
  }

  @Override
  public ExerciseActivity addExerciseActivity(long workoutId, long exerciseId) throws ExerciseNotFoundException, WorkoutNotFoundException {
    // TODO change message in exception
    ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new ExerciseNotFoundException("exercise not found"));

    // TODO change message in exception
    WorkoutEntity workoutEntity = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    ExerciseActivityEntity exerciseActivityEntity = createEmptyExerciseActivity(exerciseEntity);

    WorkoutEntity updatedWorkoutEntity = createWorkoutWithNewExerciseActivity(workoutEntity, exerciseActivityEntity);

    // TODO is there a better way to do the below?
    WorkoutEntity savedWorkoutEntity = workoutRepository.save(updatedWorkoutEntity);

    List<ExerciseActivityEntity> exerciseActivities = savedWorkoutEntity.getExerciseActivities();

    ExerciseActivityEntity lastAddedExerciseActivity = exerciseActivities.get(exerciseActivities.size() - 1);

    return exerciseActivityConverter.convert(lastAddedExerciseActivity);
  }

  @Override
  public ExerciseActivity deleteExerciseActivity(long workoutId, long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    //fetch the exerciseActivityEntity to be removed, we will return this after successful deletion
    ExerciseActivityEntity exerciseActivityToDelete = workout.getExerciseActivities()
        .stream()
        .filter(exerciseActivity -> Long.valueOf(exerciseActivityId).equals(exerciseActivity.getId()))
        .findFirst()
        .orElseThrow(() -> new ExerciseActivityNotFoundException("exercise activity not found"));

    List<ExerciseActivityEntity> exerciseActivityEntities = new ArrayList<>(workout.getExerciseActivities());
    exerciseActivityEntities.remove(exerciseActivityToDelete);

    WorkoutEntity updatedWorkout = workout.toBuilder()
        .exerciseActivities(exerciseActivityEntities)
        .build();

    //TODO check if this has to happen in this order.
    //To get this to work in this order I had to add Fetch type EAGER to exerciseSets in exerciseActivityEntity
    //Do we need to save workout, or can we directly remove exercise activity entity
    workoutRepository.save(updatedWorkout);

    return exerciseActivityConverter.convert(exerciseActivityToDelete);
  }

  private WorkoutEntity createWorkoutWithNewExerciseActivity(WorkoutEntity originalWorkoutEntity, ExerciseActivityEntity exerciseActivityEntity) {
    List<ExerciseActivityEntity> exerciseActivities = new ArrayList<>(originalWorkoutEntity.getExerciseActivities());
    exerciseActivities.add(exerciseActivityEntity);

    return originalWorkoutEntity.toBuilder()
        .exerciseActivities(exerciseActivities)
        .build();
  }

  private ExerciseActivityEntity createEmptyExerciseActivity(ExerciseEntity exerciseEntity) {
    return ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();
  }
}
