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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    Map<ExerciseEntity, ExerciseActivityEntity> exerciseActivity = new HashMap<>();

    ExerciseEntity exerciseEntity = ExerciseEntity.builder()
        .id(1L)
        .name("Bicep Curl")
        .muscleGroup(MuscleGroup.BICEP)
        .information("Bicep curl")
        .build();

    ExerciseSetEntity exerciseSetEntity = ExerciseSetEntity.builder()
        .numberOfReps(8)
        .weightKg(10D)
        .status(Status.COMPLETED)
        .build();

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .sets(Collections.singletonList(exerciseSetEntity))
        .build();

    exerciseActivity.put(exerciseEntity, exerciseActivityEntity);

    WorkoutEntity workoutEntity1 = WorkoutEntity.builder()
        .name("workout1")
        .workoutType(WorkoutType.ABS)
        .exerciseActivity(Collections.emptyMap())
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    WorkoutEntity workoutEntity2 = WorkoutEntity.builder()
        .name("workout 2")
        .workoutType(WorkoutType.LEGS)
        .exerciseActivity(exerciseActivity)
        .performedAtTimestampUtc(Instant.now())
        .notes("notes")
        .build();

    return workoutEntity2;
  }

}
