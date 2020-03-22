package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.WorkoutEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {

  List<WorkoutEntity> findAll();
}
