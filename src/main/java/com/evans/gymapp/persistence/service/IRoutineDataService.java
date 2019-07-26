package com.evans.gymapp.persistence.service;

import com.evans.gymapp.domain.Routine;

import java.util.List;
import java.util.Optional;

public interface IRoutineDataService {

  Optional<Routine> getRoutine(long routineId);

  List<Routine> getAllRoutines();
}
