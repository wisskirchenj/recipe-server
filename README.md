# IDEA EDU Course ...

Implemented in the <b>Spring Security for Java Backend Developer</b> Track of hyperskill.org's JetBrain Academy.<br>
https://hyperskill.org/projects/180

Project goal (or rather my interpretation of it) is to implement a Spring boot recipe application with Spring Security
and persisting into an H2 database.

## Technology / External Libraries

- Java 19
- Spring Boot 3.0.0
- Spring Web using "classical" WebMvC @RestController-style
- Spring Data JPA and Validate
- H2 Database
- Spring security 6.0 (coming up in later stages)
- Lombok
- Spring Tests with MockMvC
- Gradle 7.6

## Program description

The application will represent a program, that receives recipes voa REST-endpoints and stores them in a database.
We create a multi-user web service with Spring Boot that allows storing, retrieving, updating, and deleting recipes.

Currently implemented endpoints:

> <b>POST /api/recipe (unauthenticated)</b>. -> post a recipe as Json to stores it server-side.
The request JSON looks as:<pre>
{
    "name": "apple pie",
    "description": "easy apple pie",
    "ingredients": "100 g sugar, 200 g butter, 5 apples",
    "directions": "make dough, put in oven and bake"
}</pre>

> <b>GET /api/recipe (unauthenticated)</b>. -> receives a recipe that was previously send via POST.

## Project completion

[# Project was completed on 19.06.22.]: #

## Repository Contents

Sources for all project tasks (5 stages) with tests and configurations.

## Progress

20.12.22 Project started. Just setup of build and repo with gradle.

23.12.22 Stage 1 completed. Post and get endpoint /api/recipe/ with full test coverage. Setup of typical Rest-Service
package structure with mapping of Dto <-> entities
