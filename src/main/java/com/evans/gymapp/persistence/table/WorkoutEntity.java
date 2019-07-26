package com.evans.gymapp.persistence.table;

import com.evans.gymapp.domain.ExerciseActivity;
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
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn
  private Map<ExerciseEntity, ExerciseActivity> exerciseActivity;

  @NonNull
  private Instant performedAtTimestampUtc;
  
  private String notes;
}
