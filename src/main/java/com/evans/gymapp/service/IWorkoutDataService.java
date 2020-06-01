package com.evans.gymapp.service;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.request.CreateWorkoutRequest;
import com.evans.gymapp.request.EditWorkoutRequest;

import java.util.List;

public interface IWorkoutDataService {

  Workout addWorkout(CreateWorkoutRequest request);

  Workout editWorkout(long workoutId, EditWorkoutRequest request) throws WorkoutNotFoundException;

  void deleteWorkout(long workoutId) throws WorkoutNotFoundException;

  List<Workout> getAllWorkouts();

  Workout getWorkoutById(long workoutId);
}
