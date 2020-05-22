package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.persistence.entity.sets.ExerciseSetEntity;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

  @ManyToOne(fetch = FetchType.LAZY)
  private WorkoutEntity workout;

  @NonNull
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private ExerciseEntity exercise;

  @NonNull
  @Fetch(FetchMode.SUBSELECT)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "exerciseActivity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExerciseSetEntity> sets;

  private String notes;

}
