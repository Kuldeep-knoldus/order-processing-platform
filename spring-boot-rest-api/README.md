# Expense REST API

A Spring Boot 4 REST API for managing expenses. It demonstrates a layered Controller -> Service -> Repository -> MySQL architecture using Gradle, Spring Data JPA, DTO validation, pagination, search, custom queries, global exception handling, and Swagger/OpenAPI.

## Entity and technologies

`Expense` contains six fields: `id`, `description`, `category`, `amount`, `expenseDate`, and `ownerEmail`.

- Java 21
- Spring Boot 4.0.2
- Gradle 8.14+
- Spring Web MVC, Spring Data JPA, Jakarta Validation
- MySQL 8+
- Springdoc OpenAPI

## API endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| POST | `/api/expenses` | Create an expense |
| GET | `/api/expenses` | List with pagination, sorting, and filters |
| GET | `/api/expenses/{id}` | Get one expense |
| PUT | `/api/expenses/{id}` | Update an expense |
| DELETE | `/api/expenses/{id}` | Delete an expense |
| GET | `/api/expenses/summary?category=Travel` | Custom JPQL category total |
| GET | `/swagger-ui.html` | Interactive OpenAPI documentation |

Search examples:

```text
/api/expenses?category=Travel&description=hotel&page=0&size=10&sortBy=amount&direction=DESC
/api/expenses?ownerEmail=person@example.com
```

## Run locally

Create a MySQL database and user:

```sql
CREATE DATABASE expense_db;
CREATE USER 'expense_user'@'localhost' IDENTIFIED BY 'expense_password';
GRANT ALL PRIVILEGES ON expense_db.* TO 'expense_user'@'localhost';
```

Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` if your credentials differ, then run from this directory:

```powershell
gradle test
gradle bootRun
```

The API runs at `http://localhost:8090`. Hibernate manages the schema with `ddl-auto=update` for this learning assignment.

## Docker

The root [Dockerfile](Dockerfile) uses a Gradle build stage and a non-root Java 21 runtime stage. Build it from this directory with:

```powershell
docker build -t expense-api:local .
docker run --rm -p 8090:8090 `
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/expense_db `
  -e DB_USERNAME=expense_user -e DB_PASSWORD=expense_password expense-api:local
```