package com.evans.gymapp.persistence.entity.sets;

import com.evans.gymapp.domain.Status;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
//TODO change discriminator type to string
@DiscriminatorColumn(name = "set_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class ExerciseSetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  private Integer numberOfReps;

  @NonNull
  private Status status;

}
