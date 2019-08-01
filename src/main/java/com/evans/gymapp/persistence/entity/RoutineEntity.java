package com.evans.gymapp.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class RoutineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  private String name;

  @NonNull
  @OneToMany
  private List<ExerciseEntity> exercises;

  @NonNull
  private Instant lastPerformedTimestampUtc;

  private String notes;
}
