package com.evans.gymapp;

import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GymAppApplicationTests {

  @Test
  public void contextLoads() throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();

    WorkoutEntity workoutEntity = initiate();

    objectMapper.writeValueAsString(workoutEntity);


  }

  public WorkoutEntity initiate() {
    Set<ExerciseActivityEntity> exerciseActivities = new HashSet<>();

    ExerciseEntity exerciseEntity1 = ExerciseEntity.builder()
        //.id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Bicep curl")
        .build();

    ExerciseSetEntity exerciseSetEntity1 = ExerciseSetEntity.builder()
        .numberOfReps(8)
        .weightKg(10D)
        .status(Status.COMPLETED)
        .build();

    ExerciseSetEntity exerciseSetEntity2 = ExerciseSetEntity.builder()
        .numberOfReps(10)
        .weightKg(12D)
        .status(Status.FAILED)
        .build();

    ExerciseEntity exerciseEntity2 = ExerciseEntity.builder()
        //.id(2L)
        .name("Barbell Row")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Barbell Row")
        .build();

    ExerciseSetEntity exerciseSetEntity3 = ExerciseSetEntity.builder()
        .numberOfReps(12)
        .weightKg(40D)
        .status(Status.COMPLETED)
        .build();

    ExerciseActivityEntity exerciseActivityEntity1 = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity1)
        .sets(Arrays.asList(exerciseSetEntity1, exerciseSetEntity2))
        .build();

    ExerciseActivityEntity exerciseActivityEntity2 = ExerciseActivityEntity.builder()
        .exercise(exerciseEntity2)
        .sets(Collections.singletonList(exerciseSetEntity3))
        .build();

    exerciseActivities.add(exerciseActivityEntity1);
    exerciseActivities.add(exerciseActivityEntity2);

    WorkoutEntity workoutEntity1 = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivities(Collections.emptySet())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    WorkoutEntity workoutEntity2 = WorkoutEntity.builder()
        .name("workout 2")
        .workoutType(WorkoutType.LEGS)
        .exerciseActivities(exerciseActivities)
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    return workoutEntity2;
  }

}
