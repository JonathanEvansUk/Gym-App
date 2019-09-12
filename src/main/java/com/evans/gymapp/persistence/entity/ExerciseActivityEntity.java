package com.evans.gymapp.persistence.entity;

import lombok.*;
import uk.co.jemos.podam.common.PodamStrategyValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
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
  @ElementCollection
  private List<ExerciseSetEntity> sets;

  private String notes;

}
