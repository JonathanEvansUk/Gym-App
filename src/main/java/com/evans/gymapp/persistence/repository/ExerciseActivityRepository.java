package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseActivityRepository extends CrudRepository<ExerciseActivityEntity, Long> {

  List<ExerciseActivityEntity> findAll();
}
