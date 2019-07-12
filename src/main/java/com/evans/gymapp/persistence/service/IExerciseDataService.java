package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Exercise;

import java.util.List;

public interface IExerciseDataService {

  void addExercise(Exercise exercise);

  void deleteExercise(Exercise exercise);

  List<Exercise> getAllExercises();
}
