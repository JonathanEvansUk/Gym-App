package com.evans.gymapp.persistence.entity;

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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NonNull
  @ManyToOne(cascade = CascadeType.MERGE)
  private ExerciseEntity exercise;

  @NonNull
  @ElementCollection(fetch = FetchType.EAGER)
  private List<ExerciseSetEntity> sets;

  private String notes;

}
