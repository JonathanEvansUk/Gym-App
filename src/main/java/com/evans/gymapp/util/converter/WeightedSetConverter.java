package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.WeightedSet;
import com.evans.gymapp.persistence.entity.sets.WeightedSetEntity;
import org.springframework.stereotype.Component;

@Component
public class WeightedSetConverter {

  public WeightedSet convert(WeightedSetEntity weightedSetEntity) {
    return WeightedSet.builder()
        .id(weightedSetEntity.getId())
        .weightKg(weightedSetEntity.getWeightKg())
        .numberOfReps(weightedSetEntity.getNumberOfReps())
        .status(weightedSetEntity.getStatus())
        .build();
  }

  public WeightedSetEntity convert(WeightedSet weightedSet) {
    return WeightedSetEntity.builder()
        .id(weightedSet.getId())
        .weightKg(weightedSet.getWeightKg())
        .numberOfReps(weightedSet.getNumberOfReps())
        .status(weightedSet.getStatus())
        .build();
  }
}
