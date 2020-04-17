package com.evans.gymapp.config;

import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import com.evans.gymapp.util.converter.NonWeightedSetConverter;
import com.evans.gymapp.util.converter.WeightedSetConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ExerciseSetConverterConfig {

  @NonNull
  private final WeightedSetConverter weightedSetConverter;

  @NonNull
  private final NonWeightedSetConverter nonWeightedSetConverter;

  @Bean
  public Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> createFromEntityConverterMap() {
    Map<Class<? extends ExerciseSetEntity>, Function<ExerciseSetEntity, ExerciseSet>> converterMap = new HashMap<>();

    converterMap.put(WeightedSetEntity.class,
        exerciseSetEntity -> weightedSetConverter.convert((WeightedSetEntity) exerciseSetEntity));

    converterMap.put(NonWeightedSetEntity.class,
        exerciseSetEntity -> nonWeightedSetConverter.convert((NonWeightedSetEntity) exerciseSetEntity));

    return converterMap;
  }

  @Bean
  public Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> createToEntityConverterMap() {
    Map<Class<? extends ExerciseSet>, Function<ExerciseSet, ExerciseSetEntity>> converterMap = new HashMap<>();

    converterMap.put(WeightedSet.class,
        exerciseSet -> weightedSetConverter.convert((WeightedSet) exerciseSet));

    converterMap.put(NonWeightedSet.class,
        exerciseSet -> nonWeightedSetConverter.convert((NonWeightedSet) exerciseSet));

    return converterMap;
  }
}
