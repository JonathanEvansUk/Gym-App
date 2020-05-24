package com.evans.gymapp.persistence.entity.sets;


import com.evans.gymapp.domain.Status;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("WeightedSet")
public class WeightedSetEntity extends ExerciseSetEntity {

  @NonNull
  private Double weightKg;

  @Builder
  public WeightedSetEntity(Long id, ExerciseActivityEntity exerciseActivityEntity, @NonNull Integer numberOfReps, @NonNull Status status, @NonNull Double weightKg) {
    super(id, exerciseActivityEntity, numberOfReps, status);
    this.weightKg = weightKg;
  }
}
