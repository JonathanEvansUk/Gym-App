package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.ExerciseActivity;

public interface IExerciseActivityDataService {

  void updateSets(long workoutId, ExerciseActivity exerciseActivity);
}
