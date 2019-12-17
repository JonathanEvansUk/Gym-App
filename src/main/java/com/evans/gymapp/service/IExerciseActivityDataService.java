package com.evans.gymapp.service;

import com.evans.gymapp.domain.ExerciseActivity;

public interface IExerciseActivityDataService {

  void updateSets(long workoutId, ExerciseActivity exerciseActivity);
}
