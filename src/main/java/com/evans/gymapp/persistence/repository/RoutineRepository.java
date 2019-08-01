package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.persistence.entity.RoutineEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoutineRepository extends CrudRepository<RoutineEntity, Long> {

  List<RoutineEntity> findAll();
}
