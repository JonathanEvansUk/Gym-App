package com.evans.gymapp;

import com.evans.gymapp.persistence.entity.WorkoutEntity;
import org.junit.Test;
import uk.co.jemos.podam.api.*;

public class DataGenerationTest {

  @Test
  public void testPodamGeneration() {

    PodamFactory podamFactory = new PodamFactoryImpl();

    CustomStringManufacturer customStringManufacturer = new CustomStringManufacturer();
    podamFactory.getStrategy().addOrReplaceTypeManufacturer(String.class, customStringManufacturer);
    WorkoutEntity workoutEntity1 = podamFactory.manufacturePojo(WorkoutEntity.class);
  }
}
