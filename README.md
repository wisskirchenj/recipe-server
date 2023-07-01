# IDEA EDU Course

Implemented in the <b>Spring Security for Java Backend Developer</b> Track of hyperskill.org's JetBrain Academy.<br>
https://hyperskill.org/projects/180

Project goal is to implement a Spring boot recipe application with Spring Security and persisting into an H2 database.

## Technology / External Libraries

- Java 19
- Spring Boot 3.0.1 (with Spring 6.0.3 and Spring Security 6.0.1)
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

> <b>POST /api/register (unauthenticated)</b>. -> post email and password as Json to register. Returns 200 OK on success
or 400 BAD REQUEST, if invalid Json or User with email already exists.
The request JSON looks as:<pre>
{
"email": "hans.wurst@xyz.de",
"password": "topsecret",
}</pre> The password needs 8 characters, email is checked as valid format.

> <b>POST /api/recipe/new (authenticated)</b>. -> post a recipe as Json to persist it server-side with the timestamp and the creator.
The request JSON looks as:<pre>
{
    "name": "apple pie",
    "category": "bakery",
    "description": "easy apple pie",
    "ingredients": ["100 g sugar", "200 g butter", "5 apples"],
    "directions": ["make dough", "put in oven", "bake at 200°"]
}</pre> It returns the id in the database as key-value object.

> <b>GET /api/recipe/{id} (authenticated)</b>. -> receives recipe with id from the database.

> <b>DELETE /api/recipe/{id} (authenticated)</b>. -> delete recipe with id if the principal is the creator. In this case
returns 204, if not 403 FORBIDDEN is returned or 404 not Found, if recipe with id does not exist.

> <b>PUT /api/recipe/{id} (authenticated)</b>. -> receives a recipe as a JSON object and updates the recipe with specified id
if the principal is the creator. In this case returns 204, if not 403 FORBIDDEN is returned or 404 not Found, if recipe with id does not exist.
Also, updates the dateTime field.
 
> <b>GET /api/recipe/search/?category=search (authenticated)</b>.  
> <b>GET /api/recipe/search/?name=search (authenticated)</b>. -> retrieve all recipes matching to either a
case-insensitive category parameter search or a case-insensitive substring search on the name as a Json-array, which
is sorted descending by update timestamp.


## Project completion

Project was completed on 01.01.23.

## Repository Contents

Sources for all project tasks (5 stages) with tests and configurations.

## Progress

20.12.22 Project started. Just setup of build and repo with gradle.

23.12.22 Stage 1 completed. Post and get endpoint /api/recipe/ with full test coverage. Setup of typical Rest-Service
package structure with mapping of Dto <-> entities

24.12.22 Stage 2 completed. Now recipes posted are added in a list starting with id=1. The get method needs a
path variable containing the id. 404 is returned if the requested element does not exist or 400 in case the path variable 
is not in integer format.

26.12.22 Stage 3 completed. Now recipes posted are persisted in a H2 database. Also hibernate validation is performed
on input - tested with a new fancy generic and record-capable JpaUnitTestValidator. A delete endpoint is added.

27.12.22 Stage 4 completed. Now recipes posted are persisted in a H2 database. Recipe entity gets additional category and
an @UpdateTimestamp annotated LocalDateTime field. Put endpoint added to update recipe by id and a parameter search
on category or name substrings with sorting by newest posted/ updated.

01.01.23 Stage 5 completed. Added Spring security v 6.0 and a register endpoint for users to register. Only registered users
may access the api/recipe endpoints. Further update / delete actions are only permitted, if the session user is the creator
of a recipe (creator stored in recipe table).