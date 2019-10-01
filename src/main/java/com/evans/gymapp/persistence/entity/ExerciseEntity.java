package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.MuscleGroup;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  private String name;

  @Enumerated(EnumType.STRING)
  @NonNull
  private MuscleGroup muscleGroup;

  private String information;
}
