package com.evans.gymapp.service;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;

public interface IExerciseActivityDataService {

  void updateSets(long workoutId, ExerciseActivity exerciseActivity) throws ExerciseActivityNotFoundException;
}
