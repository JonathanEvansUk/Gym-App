package com.evans.gymapp.controller;

import com.evans.gymapp.domain.ExerciseActivity;
import com.evans.gymapp.exception.ExerciseActivityNotFoundException;
import com.evans.gymapp.exception.ExerciseNotFoundException;
import com.evans.gymapp.exception.WorkoutNotFoundException;
import com.evans.gymapp.service.IExerciseActivityDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.evans.gymapp.util.EndpointConstants.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(WORKOUT_BY_ID)
@RequiredArgsConstructor
public class ExerciseActivityController {

  @NonNull
  private final IExerciseActivityDataService exerciseActivityDataService;

  @PatchMapping(UPDATE_SETS)
  public void updateSets(@PathVariable long workoutId, @Valid @RequestBody ExerciseActivity exerciseActivity) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    log.info("Update sets request received: workoutId: {}, exerciseActivity: {}", workoutId, exerciseActivity);

    exerciseActivityDataService.updateSets(workoutId, exerciseActivity);
  }

  @PostMapping(ADD_EXERCISE_ACTIVITY)
  public ExerciseActivity addExerciseActivity(@PathVariable long workoutId, @RequestBody long exerciseId) throws WorkoutNotFoundException, ExerciseNotFoundException {
    log.info("Add exercise activity request received: workoutId: {}, exerciseId: {}", workoutId, exerciseId);

    return exerciseActivityDataService.addExerciseActivity(workoutId, exerciseId);
  }

  @DeleteMapping(DELETE_EXERCISE_ACTIVITY)
  public ExerciseActivity deleteExerciseActivity(@PathVariable long workoutId, @PathVariable long exerciseActivityId) throws WorkoutNotFoundException, ExerciseActivityNotFoundException {
    log.info("Delete exercise activity request received: workoutId: {}, exerciseActivityId: {}", workoutId, exerciseActivityId);

    // TODO should this return nothing?
    return exerciseActivityDataService.deleteExerciseActivity(workoutId, exerciseActivityId);
  }
}
