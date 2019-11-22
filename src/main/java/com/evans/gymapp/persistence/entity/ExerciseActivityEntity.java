package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ExerciseActivityEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @ManyToOne(cascade = CascadeType.MERGE)
  private ExerciseEntity exercise;

  @NonNull
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExerciseSetEntity> sets;

  private String notes;

}
