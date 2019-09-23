package com.evans.gymapp.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExerciseActivityNotFoundException extends Exception {

  @NonNull
  private final String message;
}
