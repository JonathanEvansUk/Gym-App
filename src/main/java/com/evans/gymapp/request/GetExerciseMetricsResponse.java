package com.evans.gymapp.request;

import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.service.impl.ExerciseDataService.WorkoutInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class GetExerciseMetricsResponse {

//  @NonNull
//  private final Integer numberOfTimesPerformed;
//
//  @NonNull
//  private final Double successRate;
//
//  @NonNull
//  private final List<Workout> workouts;

  @NonNull
  private final List<WorkoutInformation> workoutInformations;
}
