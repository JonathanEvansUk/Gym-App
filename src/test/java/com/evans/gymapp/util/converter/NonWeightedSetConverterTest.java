package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import org.junit.jupiter.api.Test;

import static com.evans.gymapp.domain.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonWeightedSetConverterTest {

  private final NonWeightedSetConverter nonWeightedSetConverter = new NonWeightedSetConverter();

  private static final long ID = 1L;
  private static final String WEIGHT = "Bodyweight";
  private static final int NUMBER_OF_REPS = 10;

  private static final NonWeightedSet NON_WEIGHTED_SET = NonWeightedSet.builder()
      .id(ID)
      .weight(WEIGHT)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  private static final NonWeightedSetEntity NON_WEIGHTED_SET_ENTITY = NonWeightedSetEntity.builder()
      .id(ID)
      .weight(WEIGHT)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  @Test
  public void convertToEntity() {
    NonWeightedSetEntity nonWeightedSetEntity = nonWeightedSetConverter.convert(NON_WEIGHTED_SET);

    assertEquals(NON_WEIGHTED_SET_ENTITY, nonWeightedSetEntity);
  }

  @Test
  public void convertFromEntity() {
    NonWeightedSet nonWeightedSet = nonWeightedSetConverter.convert(NON_WEIGHTED_SET_ENTITY);

    assertEquals(NON_WEIGHTED_SET, nonWeightedSet);
  }
}