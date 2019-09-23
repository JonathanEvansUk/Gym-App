package com.evans.gymapp.persistence.service.impl;

import com.evans.gymapp.CustomStringManufacturer;
import com.evans.gymapp.controller.ExerciseActivityNotFoundException;
import com.evans.gymapp.controller.ExerciseNotFoundException;
import com.evans.gymapp.controller.WorkoutNotFoundException;
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

import java.util.*;
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
  public ExerciseActivity addExerciseActivity(long workoutId, long exerciseId) throws ExerciseNotFoundException, WorkoutNotFoundException {
    // TODO change message in exception
    ExerciseEntity exerciseEntity = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new ExerciseNotFoundException("exercise not found"));

    // TODO change message in exception
    WorkoutEntity workoutEntity = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    ExerciseActivityEntity exerciseActivityEntity = createEmptyExerciseActivity(exerciseEntity);

    WorkoutEntity updatedWorkoutEntity = createWorkoutWithNewExerciseActivity(workoutEntity, exerciseActivityEntity);

    // TODO is there a better way to do the below?
    WorkoutEntity savedWorkoutEntity = workoutRepository.save(updatedWorkoutEntity);

    List<ExerciseActivityEntity> exerciseActivities = savedWorkoutEntity.getExerciseActivities();

    ExerciseActivityEntity lastAddedExerciseActivity = exerciseActivities.get(exerciseActivities.size() - 1);

    return exerciseActivityConverter.convert(lastAddedExerciseActivity);
  }

  @Override
  public ExerciseActivity deleteExerciseActivity(long workoutId, long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    WorkoutEntity workout = workoutRepository.findById(workoutId)
        .orElseThrow(() -> new WorkoutNotFoundException("workout not found"));

    if (!workout.getExerciseActivities().isEmpty()) {

      //fetch the exerciseActivityEntity to be removed, we will return this after successful deletion
      ExerciseActivityEntity exerciseActivityToDelete = workout.getExerciseActivities()
          .stream()
          .filter(exerciseActivity -> Objects.equals(exerciseActivity.getId(), exerciseActivityId))
          .findFirst()
          .orElseThrow(() -> new ExerciseActivityNotFoundException("exercise activity not found"));

      List<ExerciseActivityEntity> exerciseActivityEntities = new ArrayList<>(workout.getExerciseActivities());
      exerciseActivityEntities.remove(exerciseActivityToDelete);

      //      //TODO favour immutability

      workout.setExerciseActivities(exerciseActivityEntities);

      //TODO check if this has to happen in this order.
      //To get this to work in this order I had to add Fetch type EAGER to exerciseSets in exerciseActivityEntity
      //Do we need to save workout, or can we directly remove exercise actvitiy entity
      workoutRepository.save(workout);

      return exerciseActivityConverter.convert(exerciseActivityToDelete);
    }

    throw new ExerciseActivityNotFoundException("exercise activity not found");
  }

  private WorkoutEntity createWorkoutWithNewExerciseActivity(WorkoutEntity originalWorkoutEntity, ExerciseActivityEntity exerciseActivityEntity) {
    List<ExerciseActivityEntity> exerciseActivities = new ArrayList<>(originalWorkoutEntity.getExerciseActivities());
    exerciseActivities.add(exerciseActivityEntity);

    return originalWorkoutEntity.toBuilder()
        .exerciseActivities(exerciseActivities)
        .build();
  }

  private ExerciseActivityEntity createEmptyExerciseActivity(ExerciseEntity exerciseEntity) {
    return ExerciseActivityEntity.builder()
        .exercise(exerciseEntity)
        .sets(Collections.emptyList())
        .build();
  }
}
