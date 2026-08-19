# Payment System

Spring Boot payment service for creating and retrieving payment records. The application is built with Java 17, Spring Boot 3.1.3, Spring Data JPA, PostgreSQL, Liquibase, and MapStruct.

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

Update `src/main/resources/application.yml` or provide equivalent Spring configuration when these values differ. The application listens on port `8081`.

## Run the Application

From the repository root:

```bash
bash mvnw spring-boot:run
```

If the wrapper has executable permissions, the equivalent command is:

```bash
./mvnw spring-boot:run
```

Liquibase runs the configured changelog during startup. The service is then available at `http://localhost:8081`.

## Build and Test

```bash
bash mvnw clean verify
```

The test suite currently contains a Spring application context smoke test. To run it without cleaning:

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
- `src/main/resources/db/changelog/` contains Liquibase database migrations.

## Database Schema

The current changelog creates `PAYMENT_TABLE` and `CARD_TABLE`. A payment stores its order number, payment date, total amount, and a one-to-one card relationship. Hibernate is configured with `ddl-auto: update`, while Liquibase is enabled for versioned schema changes.

## Current Repository Notes

- `master.yaml` includes `db-changelog-0.0.2.xml`, but that file is not currently present in the repository.
- `PaymentController` imports `OrderDto`, but an `OrderDto` source file is not currently present.
- The Maven Wrapper is not executable in the current checkout; use `bash mvnw ...` or restore executable permissions with `chmod +x mvnw`.