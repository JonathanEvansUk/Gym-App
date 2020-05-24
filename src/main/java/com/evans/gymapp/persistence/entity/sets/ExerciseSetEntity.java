package com.evans.gymapp.persistence.entity.sets;

import com.evans.gymapp.domain.Status;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "set_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ExerciseSetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private ExerciseActivityEntity exerciseActivity;

  @NonNull
  private Integer numberOfReps;

  @NonNull
  private Status status;

}
