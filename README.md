# Payment System

REST payment-record service built with Java 17, Spring Boot 3.1.3, Spring Data JPA, PostgreSQL, Liquibase, and MapStruct. It demonstrates layered architecture, persistence, database migrations, and HTTP integration testing.

## Prerequisites

- Java 17
- PostgreSQL
- Maven, or the included Maven Wrapper

The default configuration expects a PostgreSQL instance with:

| Setting | Value |
| --- | --- |
| Host | `localhost` |
| Port | `5433` |
| Database | `postgres` |
| Username | `postgres` |
| Password | `sa` |

These values can be overridden with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. The application listens on port `8081`.

## Run the Application

From the repository root:

```bash
bash mvnw spring-boot:run
```

If the wrapper has executable permissions, the equivalent command is:

```bash
./mvnw spring-boot:run
```

Liquibase runs the versioned changelog during startup and Hibernate validates the resulting schema. The service is then available at `http://localhost:8081`.

## Build and Test

```bash
bash mvnw clean verify
```

The suite includes unit tests for the service, mapper, and models, controller tests, and a Spring integration test using an in-memory H2 database. To run it without cleaning:

```bash
bash mvnw test
```

## API

### Create a payment

```http
POST /payment/add
Content-Type: application/json
```

The controller accepts an order request containing `paymentDto`; the payment service sets `paymentDate` in UTC and persists the payment.

Example request:

```json
{
	"paymentDto": {
		"orderNumber": 42,
		"totalAmount": 19.99
	}
}
```

Example response:

```json
{
	"id": 1,
	"orderNumber": 42,
	"paymentDate": "2026-08-19T12:00:00",
	"totalAmount": 19.99
}
```

### Get a payment

```http
GET /payment/get/{id}
```

Returns the payment mapped to the requested identifier. A missing payment results in HTTP `404`.

## Architecture

The application follows a conventional layered Spring architecture:

```text
HTTP request
	-> PaymentController
	-> PaymentService
	-> PaymentMapper (MapStruct)
	-> PaymentRepository (Spring Data JPA)
	-> PostgreSQL
```

- `controller/` exposes the REST endpoints under `/payment`.
- `service/` contains payment creation and lookup rules, including UTC timestamps and not-found handling.
- `model/` contains API DTOs such as `PaymentDto` and `CardDto`.
- `mapper/` converts between DTOs and JPA entities using MapStruct-generated code.
- `entity/` contains the `Payment` and `Card` persistence models.
- `repository/` contains Spring Data repositories.
- `src/main/resources/db/changelog/` contains the Liquibase database migration and master changelog.

## Database Schema

The changelog creates `PAYMENT_TABLE` and `CARD_TABLE`, including sequences and the one-to-one payment-to-card foreign key. Liquibase owns schema changes; Hibernate runs in `validate` mode so entity and database drift is detected at startup.

## Design Notes

- The integration test uses H2 for fast, isolated endpoint testing; PostgreSQL remains the production database.
- This project stores payment records only. It is not intended to process real card data or handle production payment credentials.
- The Maven Wrapper may be run with `bash mvnw ...` when executable permissions are unavailable.