package com.evans.gymapp;

import com.evans.gymapp.persistence.entity.ExerciseEntity;
import com.evans.gymapp.persistence.entity.WorkoutEntity;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.StringTypeManufacturerImpl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CustomStringManufacturer extends StringTypeManufacturerImpl {

  private static final List<String> EXERCISES = Arrays.asList(
      "Bench Press",
      "Deadlift",
      "Dumbbell Press",
      "Crunch",
      "Overhead Press",
      "Squat",
      "Romanian Deadlift"
  );

  private static final Random RANDOM = new Random();

  @Override
  public String getType(DataProviderStrategy strategy, AttributeMetadata attributeMetadata, Map<String, Type> genericTypesArgumentsMap) {
    if (WorkoutEntity.class.isAssignableFrom(attributeMetadata.getPojoClass())) {
      return getWorkoutEntityAttributes(attributeMetadata);
    }

//    if (ExerciseEntity.class.isAssignableFrom(attributeMetadata.getPojoClass())) {
//      return getExerciseEntityAttributes(attributeMetadata);
//    }

    return super.getType(strategy, attributeMetadata, genericTypesArgumentsMap);
  }

  private String getWorkoutEntityAttributes(AttributeMetadata attributeMetadata) {

    if ("name".equals(attributeMetadata.getAttributeName())) {
      return "workout name";
    }

    return "not defined";
  }

  private String getExerciseEntityAttributes(AttributeMetadata attributeMetadata) {
    if ("name".equals(attributeMetadata.getAttributeName())) {
      return getRandomFromList(EXERCISES);
    }

    return "not defined";
  }

  private <T> T getRandomFromList(List<T> list) {
    return list.get(RANDOM.nextInt(list.size()));
  }
}
