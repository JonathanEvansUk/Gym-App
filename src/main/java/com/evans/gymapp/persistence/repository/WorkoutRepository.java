package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.table.WorkoutEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {

  Optional<WorkoutEntity> findByName(String name);
}
