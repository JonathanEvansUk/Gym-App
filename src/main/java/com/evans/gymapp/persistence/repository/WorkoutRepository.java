package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.table.WorkoutEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {

  Optional<WorkoutEntity> findByName(String name);
}
