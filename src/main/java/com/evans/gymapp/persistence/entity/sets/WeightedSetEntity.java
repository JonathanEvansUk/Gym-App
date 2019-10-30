package com.evans.gymapp.persistence.entity.sets;


import com.evans.gymapp.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//@Data
@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("2")
public class WeightedSetEntity extends ExerciseSetEntity {

  @NonNull
  private Double weightKg;

  @Builder
  public WeightedSetEntity(Long id, @NonNull Integer numberOfReps, @NonNull Status status, @NonNull Double weightKg) {
    super(id, numberOfReps, status);
    this.weightKg = weightKg;
  }
}
