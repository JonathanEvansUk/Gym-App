package com.evans.gymapp.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Workout {

    private final List<Exercise> exercises;
    private final WorkoutType workoutType;
}
