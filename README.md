# Java API Security Lab

A controlled, local API security laboratory for demonstrating, testing, and preventing Broken Object Level Authorization (BOLA) vulnerabilities with Java and Spring Boot.

The repository currently contains two implementations of the same order lookup flow:

- a deliberately vulnerable endpoint that retrieves an order only by its object identifier;
- a secured endpoint that also verifies ownership before returning the order.

> [!WARNING]
> This project contains an intentionally vulnerable endpoint for educational and defensive testing. Run it only in a local, controlled environment. Do not use the vulnerable implementation in production or test systems without explicit authorization.

## Security scenario

The database is initialized with two users and one order for each user.

| User | User ID | Owned order |
|---|---|---|
| Alice | 11111111-1111-1111-1111-111111111111 | Mechanical Keyboard |
| Bob | 22222222-2222-2222-2222-222222222222 | Gaming Monitor |

The lab simulates an authenticated identity through the X-Lab-User-Id request header. This header is not a production authentication mechanism. It is a temporary test boundary that will later be replaced with Spring Security and signed JWT validation.

### Vulnerable flow

~~~java
return repository.findById(orderId)
        .orElseThrow();
~~~

The service receives an authenticated user identifier but does not compare it with the owner of the requested order. Alice can therefore submit Bob's order ID and receive Bob's data.

~~~mermaid
flowchart TD
    A["Alice identity"] --> V["Vulnerable endpoint"]
    V --> Q["Lookup by order ID"]
    Q --> B["Bob's order"]
    B --> R["200 OK with Bob's data"]
~~~

### Secured flow

~~~java
return repository.findById(orderId)
        .filter(order -> order.belongsTo(authenticatedUserId));
~~~

The order remains available only when its owner matches the authenticated user. The API returns 404 for both a missing order and an order owned by another user, avoiding disclosure of whether the object exists.

| Request | Result |
|---|---|
| Alice requests Alice's order | 200 OK |
| Alice requests Bob's order through the vulnerable endpoint | 200 OK — BOLA reproduced |
| Alice requests Bob's order through the secured endpoint | 404 Not Found |

## Technology stack

- Java 25
- Spring Boot 4.1.1
- Spring Web MVC
- Spring JDBC and JdbcClient
- PostgreSQL 17
- Flyway
- Docker Compose
- JUnit and MockMvc
- Spring Boot Actuator

## Project structure

~~~text
src/main/java/com/enesincekara/apisecurity/lab
└── order
    ├── application
    │   ├── SecureOrderQueryService.java
    │   └── VulnerableOrderQueryService.java
    ├── domain
    │   └── CustomerOrder.java
    ├── repository
    │   ├── CustomerOrderRepository.java
    │   └── JdbcCustomerOrderRepository.java
    └── web
        ├── OrderResponse.java
        ├── SecureOrderController.java
        └── VulnerableOrderController.java
~~~

The domain object owns the order invariants and ownership rule. The repository contract separates persistence access from application behavior. The application services demonstrate the vulnerable and secured authorization decisions, while the web layer exposes both behaviors through HTTP.

## Running locally

### Requirements

- JDK 25
- Docker with Docker Compose

Start PostgreSQL:

~~~bash
docker compose up -d --wait
~~~

Run the tests:

~~~bash
./mvnw test
~~~

Start the API:

~~~bash
./mvnw spring-boot:run
~~~

Check application and database health:

~~~bash
curl http://127.0.0.1:8080/actuator/health
~~~

The application and PostgreSQL ports are bound to the loopback interface for local use.

## Reproducing the BOLA behavior

Send Alice's simulated identity while requesting Bob's order from the vulnerable endpoint:

~~~bash
curl -i \
  -H "X-Lab-User-Id: 11111111-1111-1111-1111-111111111111" \
  http://127.0.0.1:8080/api/v1/lab/vulnerable/orders/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
~~~

Expected result:

~~~text
HTTP/1.1 200 OK
~~~

The response contains an ownerId belonging to Bob. This is the intentionally reproduced authorization failure.

Run the same object-access attempt against the secured endpoint:

~~~bash
curl -i \
  -H "X-Lab-User-Id: 11111111-1111-1111-1111-111111111111" \
  http://127.0.0.1:8080/api/v1/lab/secure/orders/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
~~~

Expected result:

~~~text
HTTP/1.1 404 Not Found
~~~

## Test coverage

The current suite contains nine tests covering:

- domain invariants;
- positive and negative ownership decisions;
- JDBC mapping against PostgreSQL;
- existing and missing order queries;
- vulnerable cross-user object access;
- allowed owner access;
- denied cross-user access through the secured endpoint;
- application context startup.

PostgreSQL must be running before the integration tests are executed.

## Database migrations

Flyway applies the following migrations:

- V1__create_users_and_orders.sql creates users, orders, constraints, and the ownership index.
- V2__insert_security_lab_data.sql inserts deterministic data for Alice and Bob.

Applied migrations should not be edited. Schema changes will be introduced through new versioned migration files.

## Current security boundaries

- X-Lab-User-Id only simulates an identity established by an authentication layer.
- The vulnerable endpoint is intentional and isolated under /api/v1/lab/vulnerable.
- UUID values do not replace authorization checks.
- Parameterized JdbcClient queries are used instead of constructing SQL from request input.
- Only the health Actuator endpoint is exposed over HTTP.
- The environment is intended for local defensive security learning and testing.

## Roadmap

The next component will be an independent Java security runner that:

1. reads an authorization matrix;
2. sends controlled requests with different user and object combinations;
3. compares expected and observed access decisions;
4. records reproducible evidence for suspected BOLA findings;
5. emits machine-readable and human-readable reports;
6. runs as a security regression check in CI/CD.

Later iterations will replace the simulated identity header with Spring Security and JWT, move integration tests to isolated containers, and expand the lab with additional OWASP API Security scenarios.

## Reference

- [OWASP API1:2023 — Broken Object Level Authorization](https://owasp.org/API-Security/editions/2023/en/0xa1-broken-object-level-authorization/)
