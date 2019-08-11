package com.evans.gymapp;

import com.evans.gymapp.persistence.entity.WorkoutEntity;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class DataGenerationTest {

  @Test
  public void testPodamGeneration() {
    PodamFactory podamFactory = new PodamFactoryImpl();

    WorkoutEntity workoutEntity1 = podamFactory.manufacturePojo(WorkoutEntity.class);
  }
}
