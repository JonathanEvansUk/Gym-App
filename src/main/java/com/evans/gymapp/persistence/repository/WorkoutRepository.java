package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.table.WorkoutEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {
}
