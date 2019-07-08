package com.evans.gymapp.data.object;

import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
public class WorkoutDto {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private final String name;
    //private List<ExerciseDto>
}
