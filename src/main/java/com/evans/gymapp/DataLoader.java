package com.evans.gymapp;

import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.WorkoutType;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

  @NonNull
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final WorkoutRepository workoutRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
//    List<ExerciseEntity> exerciseEntities = generateExercises();
//
//    exerciseRepository.saveAll(exerciseEntities);
//
//    List<WorkoutEntity> workoutEntities = generateWorkoutEntities(exerciseEntities);
//
//    workoutRepository.saveAll(workoutEntities);
  }

  private List<ExerciseEntity> generateExercises() {
    ExerciseEntity bicepCurl = ExerciseEntity.builder()
        .name("Bicep Curl")
        .information("info")
        .muscleGroup(MuscleGroup.BICEP)
        .build();

    ExerciseEntity tricepPushdown = ExerciseEntity.builder()
        .name("Tricep Pushdown")
        .information("info")
        .muscleGroup(MuscleGroup.TRICEP)
        .build();

    return Arrays.asList(bicepCurl, tricepPushdown);
  }

  private List<WorkoutEntity> generateWorkoutEntities(List<ExerciseEntity> exerciseEntities) {
//    ExerciseSetEntity tricepPushdownSet1 = ExerciseSetEntity.builder()
//        .numberOfReps(8)
//        .weightKg(10D)
//        .status(Status.COMPLETED)
//        .build();
//
//    ExerciseSetEntity tricepPushdownSet2 = ExerciseSetEntity.builder()
//        .numberOfReps(8)
//        .weightKg(10D)
//        .status(Status.COMPLETED)
//        .build();
//
//    ExerciseSetEntity tricepPushdownSet3 = ExerciseSetEntity.builder()
//        .numberOfReps(6)
//        .weightKg(10D)
//        .status(Status.FAILED)
//        .build();

    List<ExerciseSetEntity> exerciseSetEntities = Collections.emptyList();
        //Arrays.asList(tricepPushdownSet1, tricepPushdownSet2, tricepPushdownSet3);

    ExerciseActivityEntity tricepPushdowns = ExerciseActivityEntity.builder()
        .exercise(exerciseEntities.get(1))
        .sets(exerciseSetEntities)
        .notes("Some notes about tricep pushdowns")
        .build();

    List<ExerciseActivityEntity> exerciseActivityEntities = Collections.singletonList(tricepPushdowns);

    WorkoutEntity workout1 = WorkoutEntity.builder()
        .name("workout 1")
        .workoutType(WorkoutType.PULL)
        .exerciseActivities(exerciseActivityEntities)
        .performedAtTimestampUtc(Instant.now())
        .notes("some notes")
        .build();

    return Collections.singletonList(workout1);
  }
}
