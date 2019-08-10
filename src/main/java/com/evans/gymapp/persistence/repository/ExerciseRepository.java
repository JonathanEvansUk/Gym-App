package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.ExerciseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseRepository extends CrudRepository<ExerciseEntity, Long> {

  List<ExerciseEntity> findAll();
}
