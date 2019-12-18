package com.evans.gymapp.service.impl;

import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.service.IWorkoutTypeService;
import org.springframework.stereotype.Service;

@Service
public class WorkoutTypeService implements IWorkoutTypeService {

  @Override
  public WorkoutType[] getWorkoutTypes() {
    return WorkoutType.values();
  }
}
