package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.service.IExerciseActivityDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import com.evans.gymapp.util.converter.ExerciseSetConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseActivityDataService implements IExerciseActivityDataService {

  @NonNull
  private final WorkoutRepository workoutRepository;

  @NonNull
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;

  @NonNull
  private final ExerciseSetConverter exerciseSetConverter;

  @Override
  // TODO decide if this is a better approach?
  public ExerciseActivity addExerciseActivity(long workoutId, long exerciseId) throws ExerciseNotFoundException, WorkoutNotFoundException {
    // TODO change message in exception
    ExerciseEntity exercise = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new ExerciseNotFoundException("exercise not found"));

    // TODO change message in exception
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    ExerciseActivityEntity newExerciseActivity = createNewExerciseActivity(exercise);

    workout.addExerciseActivity(newExerciseActivity);

    WorkoutEntity updatedWorkout = workoutRepository.save(workout);

    // need to return the persisted exerciseActivity with generated id
    ExerciseActivityEntity savedExerciseActivity = updatedWorkout.getLastExerciseActivity();

    // TODO decide what to return here. Makes sense to return the created ExerciseActivity but may be better to return Workout
    return exerciseActivityConverter.convert(savedExerciseActivity);
  }

  private ExerciseActivityEntity createNewExerciseActivity(ExerciseEntity exercise) {
    return ExerciseActivityEntity.builder()
        .exercise(exercise)
        .sets(Collections.emptyList())
        .build();
  }

  @Override
  public ExerciseActivity deleteExerciseActivity(long workoutId, long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    // TODO change message in exception
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    ExerciseActivityEntity exerciseActivityEntity = workout.getExerciseActivities()
        .stream()
        .filter(exerciseActivity -> exerciseActivity.getId() != null)
        .filter(exerciseActivity -> exerciseActivity.getId().equals(exerciseActivityId))
        .findFirst()
        .orElseThrow(() -> new ExerciseActivityNotFoundException(""));

    workout.removeExerciseActivity(exerciseActivityEntity);

    workoutRepository.save(workout);

    return exerciseActivityConverter.convert(exerciseActivityEntity);
  }

  @Override
  public void updateSets(long workoutId, ExerciseActivity exerciseActivity) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException(""));

    ExerciseActivityEntity exerciseActivityEntity = workout.getExerciseActivities()
        .stream()
        .filter(existingExerciseActivity -> existingExerciseActivity.getId() != null)
        .filter(existingExerciseActivity -> existingExerciseActivity.getId().equals(exerciseActivity.getId()))
        .findFirst()
        .orElseThrow(() -> new ExerciseActivityNotFoundException(""));

    List<ExerciseSetEntity> convertedSets = exerciseActivity.getSets().stream()
        .map(exerciseSetConverter::convert)
        .collect(Collectors.toList());

    ExerciseActivityEntity updatedExerciseActivity = exerciseActivityEntity.toBuilder()
        .sets(convertedSets)
        .build();

    updatedExerciseActivity.getSets()
        .forEach(set -> set.setExerciseActivity(updatedExerciseActivity));

    List<ExerciseActivityEntity> updatedExerciseActivities = new ArrayList<>(workout.getExerciseActivities());
    int exerciseActivityIndex = workout.getExerciseActivities().indexOf(exerciseActivityEntity);
    updatedExerciseActivities.set(exerciseActivityIndex, updatedExerciseActivity);

    WorkoutEntity updatedWorkout = workout.toBuilder()
        .exerciseActivities(updatedExerciseActivities)
        .build();

    workoutRepository.save(updatedWorkout);
  }
}
