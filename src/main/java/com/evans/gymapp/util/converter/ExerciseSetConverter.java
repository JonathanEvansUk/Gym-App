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
  private Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> fromEntityConverterMap;

  @NonNull
  private Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> toEntityConverterMap;

  // TODO add exception here if no converter found
  public ExerciseSetEntity convert(ExerciseSet exerciseSet) {
    return toEntityConverterMap.get(exerciseSet.getClass())
        .apply(exerciseSet);
  }

  // TODO add exception here if no converter found
  public ExerciseSet convert(ExerciseSetEntity exerciseSetEntity) {
    return fromEntityConverterMap.get(exerciseSetEntity.getClass())
        .apply(exerciseSetEntity);
  }
}
