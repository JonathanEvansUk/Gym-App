package com.evans.gymapp.persistence.entity.sets;


import com.evans.gymapp.domain.Status;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("WeightedSet")
public class WeightedSetEntity extends ExerciseSetEntity {

  @NonNull
  private Double weightKg;

  @Builder
  public WeightedSetEntity(Long id, @NonNull Integer numberOfReps, @NonNull Status status, @NonNull Double weightKg) {
    super(id, numberOfReps, status);
    this.weightKg = weightKg;
  }
}
