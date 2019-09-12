package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseActivityRepository extends CrudRepository<ExerciseActivityEntity, Long> {
}
