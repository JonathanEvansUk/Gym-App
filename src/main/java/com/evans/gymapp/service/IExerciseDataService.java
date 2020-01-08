package com.evans.gymapp.service;

import com.evans.gymapp.domain.Exercise;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.request.GetExerciseMetricsResponse;

import java.util.List;

public interface IExerciseDataService {

  List<Exercise> getAllExercises();

  Exercise getExerciseById(long exerciseId) throws ExerciseNotFoundException;

  GetExerciseMetricsResponse getExerciseMetricsById(long exerciseId) throws ExerciseNotFoundException;
}
