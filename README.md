# Customer Management Service

## Overview
The **Customer Management Service** is a Spring Boot–based microservice responsible for managing customer lifecycle operations within a distributed microservices architecture.

It provides APIs to create, retrieve, update, and delete customer data.  
For delete operations, it coordinates with the **Account Management Service** using **OpenFeign clients** to ensure **data consistency** across services.

This service supports both **soft delete** (logical removal) and **hard delete** (permanent removal), with recursive propagation to related account data.

---

## Key Capabilities

- Retrieve all customers
- Retrieve customer details by ID
- Create new customers
- Update existing customer details
- Soft delete customers with recursive account updates
- Hard delete customers with recursive account removal
- Inter-service communication using OpenFeign
- Centralized configuration and service discovery support

---

## API Endpoints

### 1. Get All Customers
Retrieves a list of all customers in the system.

**Endpoint**
```
GET /rest/api/customers/v1
```

**Responses**
- `200 OK` – Customers found
- `204 No Content` – No customers available

---

### 2. Get Customer by ID
Retrieves detailed information of a specific customer.

**Endpoint**
```
GET /rest/api/customers/v1/{customerId}
```

**Validation Rules**
- Customer ID must be greater than 0
- Customer must exist

**Responses**
- `200 OK` – Customer details returned
- `404 Not Found` – Customer does not exist
- `400 Bad Request` – Invalid customer ID

---

### 3. Add Customer
Creates a new customer record.

**Endpoint**
```
POST /rest/api/customers/v1
```

**Request Body**
- Customer information (validated input)

**Business Rules**
- Customer payload must be valid
- Required fields must be present

**Responses**
- `201 Created` – Customer successfully created
- `400 Bad Request` – Invalid request payload

---

### 4. Update Customer
Updates an existing customer’s details.

**Endpoint**
```
PUT /rest/api/customers/v1/{customerId}
```

**Business Rules**
- Customer must exist
- Customer ID must be valid
- Only allowed fields can be updated

**Responses**
- `202 Accepted` – Customer updated successfully
- `404 Not Found` – Customer does not exist
- `400 Bad Request` – Invalid request data

---

### 5. Soft Delete Customer (Recursive)
Performs a **logical delete** of a customer by marking the status as `R` (Removed).  
All associated accounts are also **soft deleted recursively** by invoking the **Account Management Service** using **OpenFeign**.

**Endpoint**
```
DELETE /rest/api/customers/v1/{customerId}
```

**Business Rules**
- Customer must exist
- Customer status is set to `R`
- All linked accounts are updated to status `R` via Feign client
- No data is physically removed

**Responses**
- `204 No Content` – Customer and related accounts soft deleted
- `404 Not Found` – Customer does not exist
- `400 Bad Request` – Invalid customer ID

---

### 6. Hard Delete Customer (Recursive)
Permanently deletes a customer and **all related accounts and dependent data** from the system using **OpenFeign** calls.

**Endpoint**
```
DELETE /rest/api/customers/v1/remove/{customerId}
```

**Business Rules**
- Customer must exist
- All associated accounts are deleted first via Feign client
- Customer data is permanently removed

**Responses**
- `204 No Content` – Customer and all related data deleted
- `404 Not Found` – Customer does not exist
- `400 Bad Request` – Invalid customer ID

---

## Inter-Service Communication (OpenFeign)

- OpenFeign clients are used to communicate with **Account Management Service**
- Ensures consistency during soft and hard delete operations
- Provides declarative REST client support

### Feign Use Cases
- Soft delete all accounts for a customer
- Hard delete all accounts and related data

---

## Microservices Architecture

The Customer Management Service is part of a **Spring Cloud–based microservices ecosystem**.

```
Client
│
▼
API Gateway
│
▼
Customer Management Service
│
├── Feign Client → Account Management Service
│
▼
Database
```

---

## Service Discovery (Eureka)

- Registers with **Eureka Server**
- Discovers Account Management Service dynamically
- No hardcoded service URLs

```
spring.application.name=customer-management-service
```

---

## Centralized Configuration (Config Server)

- Uses **Spring Cloud Config Server** for externalized configuration
- Supports environment-specific configuration
- Enables runtime configuration changes without redeployment

---

## Security & Authorization

- Secured using **JWT-based authentication**
- Authorization enforced at API Gateway
- Role-based access control applied
---

## Error Handling

- Global exception handling for validation and runtime errors
- Consistent error response format
- Meaningful HTTP status codes

---

## Technology Stack

- Java 17
- Spring Boot
- Spring Cloud (Eureka, Config, OpenFeign, Gateway)
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- Maven
- Relational Database

---
## Testing Strategy

- Unit tests using JUnit and Mockito
- Controller tests for API validation
- Feign client integration tests
- Security and authorization tests

---

## Author
**Customer Management Service Team**
