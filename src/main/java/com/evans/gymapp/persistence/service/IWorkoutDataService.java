package com.evans.gymapp.persistence.service;

import com.evans.gymapp.controller.*;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;

import java.util.List;
import java.util.Optional;

public interface IWorkoutDataService {

  Workout addWorkout(CreateWorkoutRequest request);

  Workout editWorkout(long workoutId, EditWorkoutRequest request) throws WorkoutNotFoundException;

  void deleteWorkout(long workoutId) throws WorkoutNotFoundException;

  List<Workout> getAllWorkouts();

  Optional<Workout> getWorkoutById(long workoutId);

  Optional<Workout> getWorkoutByName(String workoutName);

  void updateSets(long workoutId, ExerciseActivity exerciseActivity);

  ExerciseActivity addExerciseActivity(long workoutId, long exerciseId) throws ExerciseNotFoundException, WorkoutNotFoundException;

  ExerciseActivity deleteExerciseActivity(long workoutId, long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException;
}
