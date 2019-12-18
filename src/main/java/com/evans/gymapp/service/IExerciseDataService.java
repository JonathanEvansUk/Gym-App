package com.evans.gymapp.service;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;

import java.util.List;

public interface IExerciseDataService {

  List<Exercise> getAllExercises();

  Exercise getExerciseById(long exerciseId) throws ExerciseNotFoundException;
}
