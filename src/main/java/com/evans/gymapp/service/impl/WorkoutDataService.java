package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;
import com.evans.gymapp.service.IWorkoutDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
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
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

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
  public Workout getWorkoutById(long workoutId) {
    return workoutRepository.findById(workoutId)
        .map(workoutConverter::convert)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ExerciseActivity addExerciseActivity(long workoutId, long exerciseId) throws ExerciseNotFoundException, WorkoutNotFoundException {
    // TODO change message in exception
    ExerciseEntity exercise = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new ExerciseNotFoundException("exercise not found"));

    // TODO change message in exception
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    ExerciseActivityEntity newExerciseActivity = createNewExerciseActivity(exercise, workout);

    return exerciseActivityConverter.convert(exerciseActivityRepository.save(newExerciseActivity));
  }

  @Override
  public ExerciseActivity deleteExerciseActivity(long exerciseActivityId) throws ExerciseActivityNotFoundException {
    // TODO change message in exception
    ExerciseActivityEntity exerciseActivity = exerciseActivityRepository.findById(exerciseActivityId)
        .orElseThrow(() -> new ExerciseActivityNotFoundException("exercise activity not found"));

    exerciseActivityRepository.deleteById(exerciseActivityId);

    return exerciseActivityConverter.convert(exerciseActivity);
  }

  private ExerciseActivityEntity createNewExerciseActivity(ExerciseEntity exercise, WorkoutEntity workout) {
    return ExerciseActivityEntity.builder()
        .exercise(exercise)
        .workout(workout)
        .sets(Collections.emptyList())
        .build();
  }

  public static class ResourceNotFoundException extends RuntimeException {
  }
}
