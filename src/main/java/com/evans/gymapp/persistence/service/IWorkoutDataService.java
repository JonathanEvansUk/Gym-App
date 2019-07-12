package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Workout;

import java.util.List;
import java.util.Optional;

public interface IWorkoutDataService {

  void addWorkout(Workout workout);

  List<Workout> getAllWorkouts();

  Optional<Workout> getWorkout(long workoutId);

  Optional<Workout> getWorkout(String workoutName);
}
