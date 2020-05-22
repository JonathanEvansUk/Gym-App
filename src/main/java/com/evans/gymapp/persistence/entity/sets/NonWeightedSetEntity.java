package com.evans.gymapp.persistence.entity.sets;

import com.evans.gymapp.domain.Status;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
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
  public NonWeightedSetEntity(Long id, ExerciseActivityEntity exerciseActivityEntity, @NonNull Integer numberOfReps, @NonNull Status status, @NonNull String weight) {
    super(id, exerciseActivityEntity, numberOfReps, status);
    this.weight = weight;
  }
}
