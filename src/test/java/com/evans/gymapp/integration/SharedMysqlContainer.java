package com.evans.gymapp.integration;

import org.testcontainers.containers.MySQLContainer;

public class SharedMysqlContainer extends MySQLContainer {

  private static SharedMysqlContainer container;

  private SharedMysqlContainer() {
    super();
  }

  public static SharedMysqlContainer getInstance() {
    if (container == null) {
      container = new SharedMysqlContainer();
    }

    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("DB_URL", container.getJdbcUrl());
    System.setProperty("DB_USERNAME", container.getUsername());
    System.setProperty("DB_PASSWORD", container.getPassword());
  }

  @Override
  public void stop() {

  }
}
