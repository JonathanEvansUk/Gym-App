# Gym Application

Simple backend application used to store and track workouts.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

The following software are required:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Lombok](https://projectlombok.org/)
- [Maven 3](https://maven.apache.org)
- [Docker](https://www.docker.com/)

### Setting up database

You will need to ensure you have a MySQL instance running.

You will also need to create a database with name "gym_app":

```
create database gym_app;
```

You can then run the schema.sql script found at:

```
src/test/resources/schema.sql
```

### Running the application locally

To run this project from your IDE, import as a Maven project, and execute the `main` method of the  `com.evans.gymapp.GymAppApplication` class.

Alternatively you can use the Spring Boot Maven plugin like so:

```
mvn spring-boot:run
```

Once the application is running, visit the link below to fetch all workouts:

```
http://localhost:8080/workouts
```

## Running the tests

You can run the tests within an IDE, or you can use the following commands:

To run only unit tests:
```
mvn test
```

To run both unit and integration tests:
```
mvn verify -Pintegration
```

Note that you will need Docker running for the integration tests to work, as they use the
[Testcontainers](https://www.testcontainers.org/) 
library to spin up a MySQL container.

## Deployment

To package into a .jar run the below command:

```
mvn package
```

## Built With

* [Spring Boot](https://spring.io/) - Framework used to create Java applications
* [Lombok](https://projectlombok.org/) - Reduces Java boilerplate code
* [Hibernate](https://hibernate.org/) - Object-relational mapping
* [MySQL](https://www.mysql.com/) - Relational database
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Jonathan Evans**