package com.evans.gymapp.repo;

import com.evans.gymapp.domain.Workout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkoutRepository extends CrudRepository<Workout, String> {
}
