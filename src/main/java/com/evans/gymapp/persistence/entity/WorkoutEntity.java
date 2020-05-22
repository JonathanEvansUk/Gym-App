package com.evans.gymapp.persistence.entity;

import com.evans.gymapp.domain.WorkoutType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = WorkoutEntity.WORKOUT_EXERCISE_ACTIVITIES,
    attributeNodes = @NamedAttributeNode(value = "exerciseActivities",
        subgraph = WorkoutEntity.EXERCISE_ACTIVITIES_EXERCISE),
    subgraphs = @NamedSubgraph(name = WorkoutEntity.EXERCISE_ACTIVITIES_EXERCISE,
        attributeNodes = @NamedAttributeNode(value = "exercise"))
)
public class WorkoutEntity {

  public static final String WORKOUT_EXERCISE_ACTIVITIES = "workout.exerciseActivities";
  public static final String EXERCISE_ACTIVITIES_EXERCISE = "exerciseActivities.exercise";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Enumerated(EnumType.STRING)
  private WorkoutType workoutType;

  @NonNull
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExerciseActivityEntity> exerciseActivities;

  @NonNull
  private Instant performedAtTimestampUtc;

  private String notes;
}
