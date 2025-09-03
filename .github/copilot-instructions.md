# Copilot instructions for EventLime

Purpose: give AI agents the minimum context to be productive in this Spring Boot + MongoDB project.

## Architecture and code layout
- Framework: Spring Boot (parent 4.0.0-M1), Java 17 target, Maven build.
- Packages (note the capitalized segment): `com.example.EventLime.*`
	- `EventLimeApplication` – main entrypoint.
	- `controller/HomeController` – REST endpoints. Currently only `POST /signup` accepting a `User` JSON body and returning the saved entity.
	- `model/User` – `@Document(collection = "users")`, fields: `id`, `email`, `password`. Uses Lombok `@Data`. Includes a no-args constructor for Jackson.
	- `repository/UserRepository` – extends Spring Data `MongoRepository<User, String>`, includes `List<User> findByEmail(String email)`.
- Configuration: `src/main/resources/application.properties`
	- `server.port=8080`
	- MongoDB: `spring.data.mongodb.uri` (Atlas SRV) and `spring.data.mongodb.database=eventlime`.

## External dependencies and integration
- MongoDB Atlas via SRV connection string in `application.properties`. The driver is Spring Data MongoDB. Repositories are auto-scanned and wired.
- Lombok is used for model boilerplate; keep Lombok-compatible constructors (no-args + convenience as needed) for Jackson.

## Build, run, test, debug
- Use Maven Wrapper from the project root:
	- Build (skip tests): `./mvnw -DskipTests clean package`
	- Run: `./mvnw -DskipTests spring-boot:run`
	- Default port: `8080` (localhost). If Postman sees ECONNREFUSED, the app likely failed to start—check the Maven output.
- Java version: compiled with `--release 17`. Prefer running with JDK 17 to avoid toolchain issues.
- Tests: minimal (`EventLimeApplicationTests`). Adding tests is fine; stick to Maven Surefire defaults.

## API conventions in this repo
- Controllers return/accept JSON. Annotate methods with `@PostMapping(value = "/path", produces = "application/json")` and accept `@RequestBody` DTOs.
- Validation: `jakarta.validation` annotations exist on entities (`@NotNull`), but controllers currently don’t use `@Valid`. If you add validation, wire `@Valid` explicitly in controller signatures.
- Sample payload for `/signup`:
	- headers: `Content-Type: application/json`
	- body: `{ "email": "user@example.com", "password": "secret" }`

## Project-specific patterns and gotchas
- Package naming contains `EventLime` with caps; keep new classes under `com.example.EventLime` so component scanning works.
- Do not create custom repository base interfaces named `MongoRepository`; always import Spring’s `org.springframework.data.mongodb.repository.MongoRepository`.
- Entities meant for JSON must have a no-args constructor (Lombok + explicit no-args is okay).
- Secrets are currently in `application.properties`. Don’t add new secrets; prefer env vars/profiles if you must introduce configuration.

## Where to look first / exemplars
- Endpoint pattern: `controller/HomeController.java`
- Persistence pattern: `repository/UserRepository.java` and `model/User.java`
- Configuration pattern: `src/main/resources/application.properties`

## When adding features
- New endpoints: place controllers under `controller`, use Spring MVC annotations, return JSON.
- New persistence: create a model with `@Document`, add a `MongoRepository` in `repository` and inject it into controllers/services.
- Keep build working with Java 17 and Lombok; verify `./mvnw -DskipTests clean package` is green.

If anything above seems off or incomplete (e.g., more endpoints/services exist), tell me which areas to expand and I’ll refine these rules.

