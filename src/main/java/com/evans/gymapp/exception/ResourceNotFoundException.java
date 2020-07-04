package com.evans.gymapp.exception;

import lombok.Getter;

@Getter
public abstract class ResourceNotFoundException extends Exception {

  private final String message;
  private final long resourceId;

  ResourceNotFoundException(String resourceName, long resourceId) {
    this.resourceId = resourceId;
    this.message = generateExceptionMessage(resourceName, resourceId);
  }

  private String generateExceptionMessage(String resourceName, long resourceId) {
    return String.format("No %s found for id: %d", resourceName, resourceId);
  }
}
