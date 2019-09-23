package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.ExerciseActivity;

public interface IExerciseActivityDataService {

  void addExerciseActivity(ExerciseActivity exerciseActivity);

  void deleteExerciseActivity(long exerciseActivityId);
}
