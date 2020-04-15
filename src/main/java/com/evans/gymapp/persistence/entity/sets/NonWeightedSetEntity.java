package com.evans.gymapp.persistence.entity.sets;

import com.evans.gymapp.domain.Status;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("NonWeightedSet")
public class NonWeightedSetEntity extends ExerciseSetEntity {

  @NonNull
  private String weight;

  @Builder
  public NonWeightedSetEntity(Long id, @NonNull Integer numberOfReps, @NonNull Status status, @NonNull String weight) {
    super(id, numberOfReps, status);
    this.weight = weight;
  }
}
