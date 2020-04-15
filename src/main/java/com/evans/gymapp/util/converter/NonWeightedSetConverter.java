package com.evans.gymapp.util.converter;

import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.persistence.entity.sets.NonWeightedSetEntity;
import org.springframework.stereotype.Component;

@Component
public class NonWeightedSetConverter {

  public NonWeightedSet convert(NonWeightedSetEntity nonWeightedSetEntity) {
    return NonWeightedSet.builder()
        .id(nonWeightedSetEntity.getId())
        .weight(nonWeightedSetEntity.getWeight())
        .numberOfReps(nonWeightedSetEntity.getNumberOfReps())
        .status(nonWeightedSetEntity.getStatus())
        .build();
  }

  public NonWeightedSetEntity convert(NonWeightedSet nonWeightedSet) {
    return NonWeightedSetEntity.builder()
        .id(nonWeightedSet.getId())
        .weight(nonWeightedSet.getWeight())
        .numberOfReps(nonWeightedSet.getNumberOfReps())
        .status(nonWeightedSet.getStatus())
        .build();
  }
}
