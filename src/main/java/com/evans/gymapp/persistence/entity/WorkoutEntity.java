package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
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

  //TODO write tests to confirm preservation of order of insertion
  @NonNull
  @OneToMany(cascade = CascadeType.ALL)
  private List<ExerciseActivityEntity> exerciseActivities;

  @NonNull
  private Instant performedAtTimestampUtc;

  private String notes;
}
