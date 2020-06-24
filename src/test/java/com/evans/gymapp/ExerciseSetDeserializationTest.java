package com.evans.gymapp;

import com.evans.gymapp.domain.Status;
import com.evans.gymapp.domain.sets.ExerciseSet;
import com.evans.gymapp.domain.sets.NonWeightedSet;
import com.evans.gymapp.domain.sets.WeightedSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@JsonTest
public class ExerciseSetDeserializationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testDeserialize_badJson() {
    String json = "";

    assertThrows(IOException.class, () -> objectMapper.readValue(json, ExerciseSet.class));
  }

  @Test
  public void testDeserialize_weightedSet() throws IOException {
    String json = "{\"type\":\"WeightedSet\",\"id\":1,\"numberOfReps\":12,\"status\":\"COMPLETED\",\"weightKg\":10.0}";
    ExerciseSet exerciseSet = objectMapper.readValue(json, ExerciseSet.class);

    assertNotNull(exerciseSet);
    assertThat(exerciseSet, instanceOf(WeightedSet.class));

    WeightedSet weightedSet = (WeightedSet) exerciseSet;
    assertEquals(Long.valueOf(1), weightedSet.getId());
    assertEquals(Integer.valueOf(12), weightedSet.getNumberOfReps());
    assertEquals(Status.COMPLETED, weightedSet.getStatus());
    assertEquals(Double.valueOf(10D), weightedSet.getWeightKg());
  }

  @Test
  public void testDeserialize_nonWeightedSet() throws IOException {
    String json = "{\"type\":\"NonWeightedSet\",\"id\":1,\"numberOfReps\":12,\"status\":\"COMPLETED\",\"weight\":\"redBand\"}";
    ExerciseSet exerciseSet = objectMapper.readValue(json, ExerciseSet.class);

    assertNotNull(exerciseSet);
    assertThat(exerciseSet, instanceOf(NonWeightedSet.class));

    NonWeightedSet nonWeightedSet = (NonWeightedSet) exerciseSet;
    assertEquals(Long.valueOf(1L), nonWeightedSet.getId());
    assertEquals(Integer.valueOf(12), nonWeightedSet.getNumberOfReps());
    assertEquals(Status.COMPLETED, nonWeightedSet.getStatus());
    assertEquals("redBand", nonWeightedSet.getWeight());
  }
}
