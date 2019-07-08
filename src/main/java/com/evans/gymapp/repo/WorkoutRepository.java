package com.evans.gymapp.repo;

import com.evans.gymapp.data.object.WorkoutDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkoutRepository extends CrudRepository<WorkoutDto, Long> {
}
