package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ExerciseSetConverter {

  @NonNull
  private final WeightedSetConverter weightedSetConverter;

  @NonNull
  private final NonWeightedSetConverter nonWeightedSetConverter;

  private final Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> fromEntityConverterMap = createFromEntityConverterMap();
  private final Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> toEntityConverterMap = createToEntityConverterMap();

  public ExerciseSetEntity convert(ExerciseSet exerciseSet) {
    return toEntityConverterMap.get(exerciseSet.getClass())
        .apply(exerciseSet);
  }

  public ExerciseSet convert(ExerciseSetEntity exerciseSetEntity) {
    return fromEntityConverterMap.get(exerciseSetEntity.getClass())
        .apply(exerciseSetEntity);
  }

  private Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> createFromEntityConverterMap() {
    Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> converterMap = new HashMap<>();

    converterMap.put(WeightedSetEntity.class,
        exerciseSetEntity -> weightedSetConverter.convert((WeightedSetEntity) exerciseSetEntity));

    converterMap.put(NonWeightedSetEntity.class,
        exerciseSetEntity -> nonWeightedSetConverter.convert((NonWeightedSetEntity) exerciseSetEntity));

    return converterMap;
  }

  private Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> createToEntityConverterMap() {
    Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> converterMap = new HashMap<>();

    converterMap.put(WeightedSet.class,
        exerciseSet -> weightedSetConverter.convert((WeightedSet) exerciseSet));

    converterMap.put(NonWeightedSet.class,
        exerciseSet -> nonWeightedSetConverter.convert((NonWeightedSet) exerciseSet));

    return converterMap;
  }
}
