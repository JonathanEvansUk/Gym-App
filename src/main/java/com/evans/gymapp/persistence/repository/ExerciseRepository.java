package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.table.ExerciseEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<ExerciseEntity, Long> {

}
