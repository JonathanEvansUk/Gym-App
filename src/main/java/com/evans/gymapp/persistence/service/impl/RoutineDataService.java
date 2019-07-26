package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.domain.Routine;
import com.evans.gymapp.persistence.repository.RoutineRepository;
import com.evans.gymapp.persistence.service.IRoutineDataService;
import com.evans.gymapp.util.converter.RoutineConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineDataService implements IRoutineDataService {

  @NonNull
  private final RoutineRepository routineRepository;

  @NonNull
  private final RoutineConverter routineConverter;

  @Override
  public Optional<Routine> getRoutine(long routineId) {
    return routineRepository.findById(routineId)
        .map(routineConverter::convert);
  }

  @Override
  public List<Routine> getAllRoutines() {
    return routineRepository.findAll()
        .stream()
        .map(routineConverter::convert)
        .collect(Collectors.toList());
  }
}
