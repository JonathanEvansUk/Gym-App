package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class ExerciseSetConverter {

  private final Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> fromEntityConverterMap = createFromEntityConverterMap();
  private final Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> toEntityConverterMap = createToEntityConverterMap();

  private Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> createFromEntityConverterMap() {
    Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> converterMap = new HashMap<>();

    converterMap.put(WeightedSetEntity.class,
        exerciseSetEntity -> convertWeightedSetEntity((WeightedSetEntity) exerciseSetEntity));

    converterMap.put(NonWeightedSetEntity.class,
        exerciseSetEntity -> convertNonWeightedSetEntity((NonWeightedSetEntity) exerciseSetEntity));

    return converterMap;
  }

  private Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> createToEntityConverterMap() {
    Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> converterMap = new HashMap<>();

    converterMap.put(WeightedSet.class,
        exerciseSet -> convertWeightedSet((WeightedSet) exerciseSet));

    converterMap.put(NonWeightedSet.class,
        exerciseSet -> convertNonWeightedSet((NonWeightedSet) exerciseSet));

    return converterMap;
  }

  public ExerciseSetEntity convert(ExerciseSet exerciseSet) {
    return toEntityConverterMap.get(exerciseSet.getClass())
        .apply(exerciseSet);
  }

  public ExerciseSet convert(ExerciseSetEntity exerciseSetEntity) {
    return fromEntityConverterMap.get(exerciseSetEntity.getClass())
        .apply(exerciseSetEntity);
  }

  private WeightedSet convertWeightedSetEntity(WeightedSetEntity weightedSetEntity) {
    return WeightedSet.builder()
        .id(weightedSetEntity.getId())
        .weightKg(weightedSetEntity.getWeightKg())
        .numberOfReps(weightedSetEntity.getNumberOfReps())
        .status(weightedSetEntity.getStatus())
        .build();
  }

  private WeightedSetEntity convertWeightedSet(WeightedSet weightedSet) {
    return WeightedSetEntity.builder()
        .id(weightedSet.getId())
        .weightKg(weightedSet.getWeightKg())
        .numberOfReps(weightedSet.getNumberOfReps())
        .status(weightedSet.getStatus())
        .build();
  }

  private NonWeightedSet convertNonWeightedSetEntity(NonWeightedSetEntity nonWeightedSetEntity) {
    return NonWeightedSet.builder()
        .id(nonWeightedSetEntity.getId())
        .weight(nonWeightedSetEntity.getWeight())
        .numberOfReps(nonWeightedSetEntity.getNumberOfReps())
        .status(nonWeightedSetEntity.getStatus())
        .build();
  }

  private NonWeightedSetEntity convertNonWeightedSet(NonWeightedSet nonWeightedSet) {
    return NonWeightedSetEntity.builder()
        .id(nonWeightedSet.getId())
        .weight(nonWeightedSet.getWeight())
        .numberOfReps(nonWeightedSet.getNumberOfReps())
        .status(nonWeightedSet.getStatus())
        .build();
  }
}
