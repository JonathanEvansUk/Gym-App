package com.evans.gymapp.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExerciseNotFoundException extends Exception {

  @NonNull
  private final String message;
}
