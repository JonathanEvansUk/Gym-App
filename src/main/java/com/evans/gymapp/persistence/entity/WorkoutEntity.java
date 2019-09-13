package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class WorkoutEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  private String name;

  @NonNull
  private WorkoutType workoutType;

  //TODO change Set to be some ordered collection
  @NonNull
  @OneToMany(cascade = CascadeType.ALL)
  private Set<ExerciseActivityEntity> exerciseActivities;

  @NonNull
  private Instant performedAtTimestampUtc;

  private String notes;
}
