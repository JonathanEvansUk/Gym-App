package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Function;

import static com.evans.gymapp.domain.Status.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExerciseSetConverterTest {

  private ExerciseSetConverter exerciseSetConverter;

  @Mock
  private Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> fromEntityConverterMap;

  @Mock
  private Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> toEntityConverterMap;

  @Mock
  private Function<ExerciseSet, ExerciseSetEntity> toEntityConverter;

  @Mock
  private Function<ExerciseSetEntity, ExerciseSet> fromEntityConverter;

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

  @BeforeEach
  public void setUp() {
    exerciseSetConverter = new ExerciseSetConverter(fromEntityConverterMap, toEntityConverterMap);
  }

  @Test
  public void convertToEntity() {
    given(toEntityConverterMap.get(WeightedSet.class))
        .willReturn(toEntityConverter);

    given(toEntityConverter.apply(WEIGHTED_SET))
        .willReturn(WEIGHTED_SET_ENTITY);

    ExerciseSetEntity exerciseSetEntity = exerciseSetConverter.convert(WEIGHTED_SET);

    assertEquals(WEIGHTED_SET_ENTITY, exerciseSetEntity);

    verify(toEntityConverterMap).get(WeightedSet.class);
    verify(toEntityConverter).apply(WEIGHTED_SET);
  }

  @Test
  public void convertFromEntity() {
    given(fromEntityConverterMap.get(WeightedSetEntity.class))
        .willReturn(fromEntityConverter);

    given(fromEntityConverter.apply(WEIGHTED_SET_ENTITY))
        .willReturn(WEIGHTED_SET);

    ExerciseSet exerciseSet = exerciseSetConverter.convert(WEIGHTED_SET_ENTITY);

    assertEquals(WEIGHTED_SET, exerciseSet);

    verify(fromEntityConverterMap).get(WeightedSetEntity.class);
    verify(fromEntityConverter).apply(WEIGHTED_SET_ENTITY);
  }
}