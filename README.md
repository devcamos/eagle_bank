# Eagle Bank API

A Java 21 / Spring Boot 3.5 REST API for a simple bank: users, bank accounts and deposit/withdraw transactions, with validation, consistent error responses and OpenAPI docs. Built as a backend practice project.

## Features

- **Users:** create, read, update and delete users.
- **Bank accounts:** open, read, update and close accounts linked to a user.
- **Transactions:** deposit, withdraw, look up a transaction and list an account's history. Withdrawals that would overdraw are rejected (`InsufficientFundsException`).
- **DTO-driven contracts:** requests and responses use dedicated DTOs, never JPA entities.
- **Validation and errors:** Bean Validation on inputs; a global exception handler returns consistent error bodies (404 for missing resources, 4xx for rule breaks).
- **OpenAPI:** Swagger UI via springdoc.

## Stack

- Java 21, Spring Boot 3.5 (Web, Data JPA, Validation)
- H2 in-memory database (no setup needed)
- springdoc-openapi for API docs
- JUnit 5 + Spring Boot Test (controller and service tests with JSON payload fixtures)

## Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/v1/users` | Create user |
| GET / PUT / DELETE | `/v1/users/{id}` | Read, update, delete user |
| POST | `/v1/accounts` | Open account |
| GET / PUT / DELETE | `/v1/accounts/{id}` | Read, update, close account |
| POST | `/v1/accounts/{accountId}/transactions/deposit` | Deposit |
| POST | `/v1/accounts/{accountId}/transactions/withdraw` | Withdraw |
| GET | `/v1/accounts/{accountId}/transactions` | Account history |
| GET | `/v1/transactions/{transactionId}` | Single transaction |

Full, interactive docs: `http://localhost:8080/swagger-ui/index.html` once the app is running.

## Run locally

Prerequisites: Java 21 (the Maven wrapper downloads Maven for you).

```sh
./mvnw spring-boot:run
```

The API listens on `http://localhost:8080`.

## Tests

```sh
./mvnw test
```

## Project structure

- `src/main/java/com/eaglebank/` – controllers, services, repositories, DTOs, entities, exceptions
- `src/test/java/com/eaglebank/` – controller and service tests
- `src/test/resources/payloads/` – JSON payloads used by tests

## Status

Practice project: not deployed, no authentication layer, data is in-memory and resets on restart.
