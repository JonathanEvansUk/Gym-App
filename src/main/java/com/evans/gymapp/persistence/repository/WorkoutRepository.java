package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.WorkoutEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {

  List<WorkoutEntity> findAll();

  Optional<WorkoutEntity> findByName(String name);
}
