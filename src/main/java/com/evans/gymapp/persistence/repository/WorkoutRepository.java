package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import static com.evans.gymapp.persistence.entity.WorkoutEntity.WORKOUT_EXERCISE_ACTIVITIES;

public interface WorkoutRepository extends CrudRepository<WorkoutEntity, Long> {

  @EntityGraph(value = WORKOUT_EXERCISE_ACTIVITIES)
  List<WorkoutEntity> findAll();
}
