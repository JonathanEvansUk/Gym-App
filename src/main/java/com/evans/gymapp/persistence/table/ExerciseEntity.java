package com.evans.gymapp.persistence.table;

import com.evans.gymapp.domain.MuscleGroup;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

  @NonNull
  private MuscleGroup muscleGroup;

  private String information;
}
