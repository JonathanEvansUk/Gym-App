package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.CustomStringManufacturer;
import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.domain.Workout;
import com.evans.gymapp.persistence.entity.ExerciseActivityEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
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

import javax.annotation.PostConstruct;
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
  private final WorkoutConverter workoutConverter;

  @NonNull
  private final ExerciseActivityConverter exerciseActivityConverter;

  @PostConstruct
  public void initiate() {
    PodamFactory podamFactory = new PodamFactoryImpl();

    podamFactory.getStrategy().addOrReplaceTypeManufacturer(String.class, new CustomStringManufacturer());
    //  WorkoutEntity workoutEntity = podamFactory.manufacturePojo(WorkoutEntity.class);

//    workoutRepository.save(workoutEntity);

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
  public void updateSets(long workoutId, ExerciseActivity newExerciseActivity) {
    Optional<WorkoutEntity> currentWorkout = workoutRepository.findById(workoutId);

    if (currentWorkout.isPresent()) {
      ExerciseActivityEntity newExerciseActivityEntity = exerciseActivityConverter.convert(newExerciseActivity);

      WorkoutEntity workoutEntity = currentWorkout.get();

      // Add new exercise activity to existing collection
      Set<ExerciseActivityEntity> exerciseActivityEntities = workoutEntity.getExerciseActivities().stream()
          .filter(exerciseActivityEntity -> !exerciseActivityEntity.getId().equals(newExerciseActivity.getId()))
          .collect(Collectors.toSet());

      exerciseActivityEntities.add(newExerciseActivityEntity);

      workoutEntity.setExerciseActivities(exerciseActivityEntities);

      //exerciseRepository.save(newExerciseActivityEntity.getExercise());


//      workoutRepository.save(workoutEntity);
    }
  }
}
