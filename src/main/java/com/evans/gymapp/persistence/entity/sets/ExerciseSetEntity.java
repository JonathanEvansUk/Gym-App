package com.evans.gymapp.persistence.entity.sets;

import com.evans.gymapp.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "set_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ExerciseSetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  private Integer numberOfReps;

  @NonNull
  private Status status;

}
