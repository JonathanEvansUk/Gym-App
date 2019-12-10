package com.evans.gymapp.exception;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExerciseActivityNotFoundException extends Exception {

  @NonNull
  private final String message;
}
