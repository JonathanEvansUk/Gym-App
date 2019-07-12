package com.evans.gymapp.persistence.table;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class WorkoutEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  private String name;

  @NonNull
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn
  private List<ExerciseEntity> exercises;
}
