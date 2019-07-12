package com.evans.gymapp.persistence.table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
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
