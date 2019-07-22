package com.evans.gymapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class Routine {

  private final String name;
  private final List<Exercise> exercises;
  private final Instant lastPerformedTimestampUtc;
  private final String notes;
}
