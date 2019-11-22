package com.evans.gymapp.persistence.repository;

import com.evans.gymapp.domain.MuscleGroup;
import com.evans.gymapp.domain.Status;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ExerciseActivityRepositoryTest {

  @Autowired
  private ExerciseActivityRepository exerciseActivityRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  private static final ExerciseEntity BICEP_CURL = ExerciseEntity.builder()
      .id(1L)
      .muscleGroup(MuscleGroup.BICEP)
      .name("Bicep Curl")
      .information("Information")
      .build();

  @BeforeEach
  public void addBicepCurl() {
    exerciseRepository.save(BICEP_CURL);
  }

  @Test
  public void addPolymorphicTypes() {
    ExerciseSetEntity weightedSet = WeightedSetEntity.builder()
        .weightKg(12D)
        .numberOfReps(8)
        .status(Status.COMPLETED)
        .build();

    ExerciseSetEntity nonWeightedSet = NonWeightedSetEntity.builder()
        .weight("Red Band")
        .numberOfReps(10)
        .status(Status.COMPLETED)
        .build();

    List<ExerciseSetEntity> sets = Arrays.asList(weightedSet, nonWeightedSet);

    ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
        .id(1L)
        .exercise(BICEP_CURL)
        .sets(sets)
        .notes("Notes")
        .build();

    ExerciseActivityEntity savedExerciseActivity = exerciseActivityRepository.save(exerciseActivityEntity);

    List<ExerciseSetEntity> savedSets = savedExerciseActivity.getSets();

    assertNotNull(savedSets);
    assertEquals(2, savedSets.size());

    assertThat(savedSets.get(0), instanceOf(WeightedSetEntity.class));
    assertThat(savedSets.get(1), instanceOf(NonWeightedSetEntity.class));
  }
}