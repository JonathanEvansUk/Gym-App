package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.junit.jupiter.api.Test;

import static com.evans.gymapp.domain.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeightedSetConverterTest {

  private final WeightedSetConverter weightedSetConverter = new WeightedSetConverter();

  private static final long ID = 1L;
  private static final double WEIGHT_KG = 5D;
  private static final int NUMBER_OF_REPS = 10;

  private static final WeightedSet WEIGHTED_SET = WeightedSet.builder()
      .id(ID)
      .weightKg(WEIGHT_KG)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  private static final WeightedSetEntity WEIGHTED_SET_ENTITY = WeightedSetEntity.builder()
      .id(ID)
      .weightKg(WEIGHT_KG)
      .numberOfReps(NUMBER_OF_REPS)
      .status(COMPLETED)
      .build();

  @Test
  public void convertToEntity() {
    WeightedSetEntity weightedSetEntity = weightedSetConverter.convert(WEIGHTED_SET);

    assertEquals(WEIGHTED_SET_ENTITY, weightedSetEntity);
  }

  @Test
  public void convertFromEntity() {
    WeightedSet weightedSet = weightedSetConverter.convert(WEIGHTED_SET_ENTITY);

    assertEquals(WEIGHTED_SET, weightedSet);
  }
}