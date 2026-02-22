# Spring boot Kotlin Starter Service
This service contains almost all tools needed to build a basic enterprise level application on Spring boot with Kotlin

# Spring Boot Kotlin Backend Starter

A production-style backend service built with **Kotlin + Spring Boot**, following clean architecture principles, CI/CD practices, and modern software engineering standards.

---

## Tech Stack

- Kotlin 1.9
- Spring Boot 3.2
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL (runtime)
- H2 (test profile)
- Liquibase (database migrations)
- Spring Security
- JUnit 5 + Mockito
- JaCoCo (coverage rules)
- Docker
- GitHub Actions CI
- GHCR (Docker registry)

---

## Architecture Overview

The project follows a layered architecture:


Controller → Service → Repository → Database


### Controller Layer

- Handles HTTP requests
- Performs request validation
- Returns DTO responses
- Does **NOT** contain business logic
- Fully unit-tested using MockMvc

### Service Layer

- Contains business logic
- Orchestrates repositories
- Handles exception translation
- Converts Entities ↔ DTOs
- 100% coverage enforced via JaCoCo profile

### Repository Layer

- Spring Data JPA repositories
- Interface-based
- Hibernate handles SQL generation
- Connected to MySQL in runtime

---

## Database Strategy

### Runtime Database

- MySQL
- Managed via Spring Data JPA
- Hibernate as ORM provider

### Test Database

- H2 in-memory
- Configured via test profile
- Isolated test runs
- Fast CI execution

---

## Database Migrations (Liquibase)

We use **Liquibase** to manage schema changes.

### Why Liquibase?

- Version-controlled schema evolution
- Repeatable migrations
- Environment-safe
- Works in CI/CD

### Migration Files

Stored under:


src/main/resources/migrations/


Test-specific changelog:


db.changelog-test.xml


Liquibase runs automatically at startup.

---

## Hibernate & JPA

- Entities are annotated with `@Entity`
- Relationships use `@ManyToMany`, `@JoinTable`, etc.
- Kotlin JPA plugins:
    - `all-open`
    - `no-arg`
- Lazy vs eager loading decisions are explicit
- DTO layer separates persistence from API contract

---

## Testing Strategy

### Unit Tests

- Service layer tested with Mockito
- Repository layer mocked
- Exception scenarios covered
- Custom exception verification

### Controller Tests

- Standalone MockMvc setup
- Validates:
    - Status codes
    - Validation behavior
    - Service interaction

### Security Filter Tests

- `PreAuthorizationFilter` tested independently
- `MockHttpServletRequest` based
- Header validation scenarios covered

### Coverage Strategy

- Service classes → **100% required**
- Controller classes → **100% required**
- Whole project → **70% minimum**
- Coverage enforced only via `-Pcoverage` profile

Run coverage locally:


mvn clean verify -Pcoverage


---

## Security

- Custom `PreAuthorizationFilter`
- Stateless session policy
- SecurityContext usage
- Separation of filter logic from configuration
- Filter tested independently

---

## Docker

Build Docker image:


docker build -t spring-boot-kotlin-service .


CI pipeline automatically builds and publishes image to:


ghcr.io/<username>/<repo>


---

## CI/CD

Implemented using **GitHub Actions**.

Pipeline performs:

- Checkout
- JDK setup
- Maven build
- Test execution
- Docker build
- Push to GitHub Container Registry

Coverage enforcement can be toggled via profile.

---

## How To Run

### Run locally (dev)

mvn spring-boot:run


### Run tests


mvn test


### Run with coverage enforcement


mvn clean verify -Pcoverage


### Run via Docker


docker build -t app .
docker run -p 8080:8080 app


---

## 📌 Engineering Principles Applied

- Separation of concerns
- Dependency inversion
- DTO isolation
- Environment-driven configuration
- Version-controlled migrations
- Deterministic builds
- CI-based verification
- Reproducible container builds

---

## Why This Project Matters

This project demonstrates:

- API design and layering
- Database integration (MySQL + Hibernate)
- Migration strategy (Liquibase)
- Security filter implementation
- Clean test architecture
- Coverage governance
- Containerization
- CI/CD integration  