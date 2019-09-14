package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.CustomStringManufacturer;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import com.evans.gymapp.persistence.repository.ExerciseActivityRepository;
import com.evans.gymapp.persistence.repository.ExerciseRepository;
import com.evans.gymapp.persistence.repository.WorkoutRepository;
import com.evans.gymapp.persistence.service.IWorkoutDataService;
import com.evans.gymapp.util.converter.ExerciseActivityConverter;
import com.evans.gymapp.util.converter.WorkoutConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutDataService implements IWorkoutDataService {

  @NonNull
  private final WorkoutRepository workoutRepository;

  @NonNull
  //TODO remove this?
  private final ExerciseRepository exerciseRepository;

  @NonNull
  private final ExerciseActivityRepository exerciseActivityRepository;

  @NonNull
  private final WorkoutConverter workoutConverter;

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;

  //@PostConstruct
  public void initiate() {
//    addExercises();

    PodamFactory podamFactory = new PodamFactoryImpl();

    podamFactory.getStrategy().addOrReplaceTypeManufacturer(String.class, new CustomStringManufacturer());

    List<WorkoutEntity> workoutEntities = IntStream.range(0, 5)
        .mapToObj(i -> podamFactory.manufacturePojo(WorkoutEntity.class))
        .collect(Collectors.toList());


    workoutRepository.saveAll(workoutEntities);
  }

  @Override
  //TODO maybe return boolean to indicate successful creation?
  public void addWorkout(Workout workout) {
    WorkoutEntity workoutEntity = workoutConverter.convert(workout);

    workoutRepository.save(workoutEntity);
  }

  @Override
  public List<Workout> getAllWorkouts() {
    return workoutRepository.findAll()
        .stream()
        .map(workoutConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Workout> getWorkoutById(long workoutId) {
    return workoutRepository.findById(workoutId)
        .map(workoutConverter::convert);
  }

  @Override
  public Optional<Workout> getWorkoutByName(String workoutName) {
    return workoutRepository.findByName(workoutName)
        .map(workoutConverter::convert);
  }

  @Override
  public void updateSets(long workoutId, ExerciseActivity exerciseActivity) {
    ExerciseActivityEntity exerciseActivityEntity = exerciseActivityConverter.convert(exerciseActivity);
    exerciseActivityRepository.save(exerciseActivityEntity);
  }

  @Override
  public Optional<ExerciseActivity> addExerciseActivity(long workoutId, long exerciseId) {
    Optional<ExerciseEntity> exerciseEntity = exerciseRepository.findById(exerciseId);

    if (exerciseEntity.isPresent()) {
      ExerciseActivityEntity exerciseActivityEntity = ExerciseActivityEntity.builder()
          .exercise(exerciseEntity.get())
          .sets(Collections.emptyList())
          .build();

      // TODO not sure if need to save exerciseActivity first before workout
      // maybe cascade will work something to try in future
      exerciseActivityRepository.save(exerciseActivityEntity);

      Optional<WorkoutEntity> workoutEntity = workoutRepository.findById(workoutId);

      if (workoutEntity.isPresent()) {
        // TODO should probably update this to favour immutability
        WorkoutEntity updatedWorkoutEntity = workoutEntity.get();

        List<ExerciseActivityEntity> exerciseActivities = updatedWorkoutEntity.getExerciseActivities();

        exerciseActivities.add(exerciseActivityEntity);

        workoutRepository.save(updatedWorkoutEntity);

        ExerciseActivity exerciseActivity = exerciseActivityConverter.convert(exerciseActivityEntity);

        return Optional.of(exerciseActivity);
      }

      //else throw some exception saying workout not found
    }


    //Maybe throw exception here to say no exercise found for given id?
    return Optional.empty();
  }
}
