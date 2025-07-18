# Eagle Bank API

A robust, best-practice Java/Spring Boot REST API for managing users, bank accounts, and transactions in a banking context.

## Features
- **User Management:** Create, update, retrieve, and delete users.
- **Bank Account Management:** Open, update, retrieve, and close bank accounts linked to users.
- **Transaction Management:** Deposit, withdraw, and view transaction history for accounts. All business rules enforced (e.g., no overdrafts).
- **DTO-Driven:** All input/output uses Data Transfer Objects (DTOs) for clear API contracts.
- **Validation:** Strong validation on all endpoints with clear error messages.
- **Exception Handling:** Global exception handler for consistent error responses.
- **OpenAPI/Swagger:** All endpoints documented and grouped for easy exploration in Swagger UI.
- **Test Coverage:** Comprehensive tests for controllers and services, with test data loaded from JSON files.

## API Documentation
- Interactive API docs available at `/swagger-ui.html` or `/swagger-ui/` when the app is running.
- All endpoints are grouped and described for easy use.

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Running the App
```sh
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080` by default.

### Running Tests
```sh
./mvnw test
```

## Project Structure
- `src/main/java/com/eaglebank/` — Main application code
- `src/test/java/com/eaglebank/` — Tests
- `src/test/resources/payloads/` — JSON payloads for tests

## Contributing
Pull requests are welcome! Please ensure all tests pass and code is well-documented.

---

**Eagle Bank API** — Secure, reliable, and developer-friendly banking API. 