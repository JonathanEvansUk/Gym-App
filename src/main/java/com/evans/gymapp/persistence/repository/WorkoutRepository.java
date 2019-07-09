package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.table.WorkoutTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends CrudRepository<WorkoutTable, Long> {
}
