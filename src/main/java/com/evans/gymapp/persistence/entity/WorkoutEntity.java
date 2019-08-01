package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

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

  @NonNull
  @OneToMany
  private Map<ExerciseEntity, ExerciseActivityEntity> exerciseActivity;

  @NonNull
  private Instant performedAtTimestampUtc;

  private String notes;
}
