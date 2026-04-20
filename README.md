# Employee Documents API

CRUD REST API for managing employee documents (PAN, Joining Letter, Offer Letter, and other dynamic types) built with Java 17 + Spring Boot 3.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.2.5 |
| Persistence | Spring Data JPA + Hibernate |
| Database | H2 (in-memory) |
| Validation | Spring Boot Validation (`@Valid`) |
| Boilerplate | Lombok |
| Build | Maven |
| Java | 26 |

---

## Project Structure

```
src/main/java/com/pratik/employeedocs/
├── EmployeeDocsApplication.java       ← entry point
├── controller/
│   └── DocumentController.java        ← HTTP layer
├── service/
│   ├── DocumentService.java           ← interface (abstraction)
│   └── impl/
│       └── DocumentServiceImpl.java   ← business logic
├── repository/
│   └── DocumentRepository.java        ← JPA data access
├── entity/
│   └── Document.java                  ← JPA entity
├── dto/
│   ├── DocumentRequest.java           ← input DTO
│   ├── DocumentResponse.java          ← output DTO
│   └── ApiResponse.java               ← consistent response wrapper
└── exception/
    ├── GlobalExceptionHandler.java    ← @RestControllerAdvice
    └── ResourceNotFoundException.java
```

---

## Architecture

**Layered Architecture**: `Controller → Service (interface) → Repository → DB`

- **Controller**: handles HTTP requests, delegates all logic to service
- **Service interface**: abstraction layer — controller depends on interface, not implementation
- **ServiceImpl**: concrete business logic, injected as singleton via `@Service` + `@RequiredArgsConstructor`
- **Repository**: Spring Data JPA — no boilerplate SQL needed
- **DTO pattern**: `DocumentRequest` for input, `DocumentResponse` for output — the `Document` entity is never exposed directly to the client
- **Global exception handler**: single `@RestControllerAdvice` class catches `ResourceNotFoundException` (404), validation errors (400), and any unexpected errors (500)
- **Consistent response wrapper**: every endpoint returns `ApiResponse<T> { success, message, data }`

---

## Setup & Run

**Prerequisites**: Java 17+, Maven 3.6+

```bash
# Clone and run
mvn spring-boot:run
```

App starts at: `http://localhost:8081`  
H2 Console: `http://localhost:8081/h2-console`  
JDBC URL: `jdbc:h2:mem:employeedocs` (username: `sa`, password: blank)

---

## API Reference

### Base URL: `http://localhost:8081/api/v1/documents`

---

### 1. Create Document — `POST /api/v1/documents`

**Request Body:**
```json
{
  "employeeId": "EMP001",
  "employeeName": "Pratik Sharma",
  "documentType": "PAN",
  "documentNumber": "ABCDE1234F",
  "fileUrl": "https://example.com/pan.pdf",
  "remarks": "Verified"
}
```

**Response: `201 Created`**
```json
{
  "success": true,
  "message": "Document created",
  "data": {
    "id": 1,
    "employeeId": "EMP001",
    "employeeName": "Pratik Sharma",
    "documentType": "PAN",
    "documentNumber": "ABCDE1234F",
    "fileUrl": "https://example.com/pan.pdf",
    "remarks": "Verified",
    "createdAt": "2026-04-20T14:56:26",
    "updatedAt": "2026-04-20T14:56:26"
  }
}
```

![Create Document](screenshots/01-create.png)

---

### 2. Get Document by ID — `GET /api/v1/documents/{id}`

**Response: `200 OK`**
```json
{
  "success": true,
  "message": "Document fetched",
  "data": { ... }
}
```

![Get by ID](screenshots/02-get-by-id.png)

---

### 3. Get All Documents (Paginated) — `GET /api/v1/documents`

**Query Parameters:**

| Param | Default | Description |
|---|---|---|
| `page` | 0 | Page number (0-based) |
| `size` | 10 | Items per page |
| `sortBy` | createdAt | Field to sort by |
| `sortDir` | desc | `asc` or `desc` |
| `employeeId` | — | Filter by employee ID |
| `documentType` | — | Filter by document type |

**Example:** `GET /api/v1/documents?page=0&size=10`

**Response: `200 OK`**
```json
{
  "success": true,
  "message": "Documents fetched",
  "data": {
    "content": [...],
    "totalElements": 1,
    "totalPages": 1,
    "number": 0,
    "size": 10
  }
}
```

![Get All Paginated](screenshots/03-get-all-paginated.png)

---

### 4. Filter by Employee ID — `GET /api/v1/documents?employeeId=EMP001`

**Response: `200 OK`** — returns only documents for that employee

![Filter by Employee](screenshots/04-filter-by-employee.png)

---

### 5. Update Document — `PUT /api/v1/documents/{id}`

**Request Body:** same as Create

**Response: `200 OK`**
```json
{
  "success": true,
  "message": "Document updated",
  "data": { ... }
}
```

![Update Document](screenshots/05-update.png)

---

### 6. Delete Document — `DELETE /api/v1/documents/{id}`

**Response: `200 OK`**
```json
{
  "success": true,
  "message": "Document deleted",
  "data": null
}
```

![Delete Document](screenshots/06-delete.png)

---

## HTTP Status Codes

| Code | Scenario |
|---|---|
| `200 OK` | GET, PUT, DELETE success |
| `201 Created` | POST success |
| `400 Bad Request` | Validation failure (missing/blank required fields) |
| `404 Not Found` | Document ID does not exist |
| `500 Internal Server Error` | Unexpected server error |

---

## Assumptions & Design Decisions

1. **H2 in-memory database** — zero setup for local development. Swap `application.properties` datasource config for MySQL/PostgreSQL in production.
2. **Dynamic document types** — `documentType` is a free-text `String` (not an enum) to support any document type beyond PAN/Joining Letter without code changes.
3. **No file storage** — `fileUrl` stores a URL reference only; actual binary file upload is out of scope.
4. **Hard delete** — `DELETE` permanently removes the record. Add a `deletedAt` timestamp for soft delete if needed.
5. **Interface-based service** — `DocumentController` depends on the `DocumentService` interface, not the implementation, making it easy to swap or mock.
