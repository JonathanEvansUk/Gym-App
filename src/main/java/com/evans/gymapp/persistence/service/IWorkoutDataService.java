package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;

import java.util.List;
import java.util.Optional;

public interface IWorkoutDataService {

  void addWorkout(Workout workout);

  List<Workout> getAllWorkouts();

  Optional<Workout> getWorkoutById(long workoutId);

  Optional<Workout> getWorkoutByName(String workoutName);

  void updateSets(long workoutId, ExerciseActivity exerciseActivity);

  Optional<ExerciseActivity> addExerciseActivity(long workoutId, long exerciseId);
}
