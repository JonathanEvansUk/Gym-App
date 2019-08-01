package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.ExerciseEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends CrudRepository<ExerciseEntity, Long> {

}
