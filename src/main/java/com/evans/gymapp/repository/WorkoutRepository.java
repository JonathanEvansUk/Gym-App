package com.evans.gymapp.repository;

import com.evans.gymapp.domain.Workout;
import org.springframework.data.repository.Repository;

public interface WorkoutRepository extends Repository<Workout, String> {
}
