# Resolve — Task Management Service

Task Overview: User Task Management Service
Create a simple RESTful application in Java for managing tasks assigned to users. Each task
has a title, description, due date, status, and assignee (user). The service should support
creating users, assigning tasks, and querying task statuses. The tasks might depend on
another task. If a task is not complete, you can’t switch to completed state the tasks that
depend on it. The application must persist data to an in-memory database (like H2) or
MariaDB/PostgreSQL database hosted on docker container. Include unit and integration
tests.

## Technical Requirements

## Database Requirements
Use H2 in-memory or MariaDB/PostgreSQL database with JPA:
- User entity: id, name, email
- Task entity: id, title, description, dueDate, status, userId, dependsOn
Use Flyway or schema auto-generation.

## Testing Requirements
Use JUnit 5 and Mockito:
- Unit tests for service layer
- Integration tests for controller endpoints
- Validate expected HTTP status codes and payloads
- Include edge cases: invalid IDs, empty fields, etc.

## Deliverables
- Source code in a clean Maven or Gradle project structure in a GitHub repository
- README with:
- Setup &amp; build instructions
- Sample curl/Postman commands to test endpoints
- Postman collection (optional but encouraged)

## Description
Simple RESTful Spring Boot application for managing user tasks.  
Each task has a title, description, due date, status and assignee.  
Tasks can depend on another task: you cannot mark a task as completed if its dependency is not yet completed.  
Data is persisted in-memory H2
Includes unit and integration tests.

## Database Requirements
Use H2 in-memory or MariaDB/PostgreSQL database with JPA:
- User entity: id, name, email
- Task entity: id, title, description, dueDate, status, userId, dependsOn
Use Flyway or schema auto-generation.

## Endpoints

### Users
- **POST** `/users` — create a new user
- **PUT** `/users/{id}` — update an existing user
- **GET** `/users` — list all users

### Tasks
- **POST** `/tasks` — create a new task
- **GET** `/tasks` — list all tasks
- **GET** `/tasks/{id}` — get task by ID
- **PUT** `/tasks/{id}` — update task status or assignee
- **GET** `/tasks/user/{userId}` — list tasks assigned to a specific user

### Admin
- **DELETE** `/admin/clear` — clear all users and tasks

## Postman
There is a `postman_collection.json` file in the project root for importing into Postman.  

## Swagger
http://localhost:8080/swagger-ui/index.html


