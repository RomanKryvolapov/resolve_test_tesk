# Resolve — Task Management Service

## Description
Simple RESTful Spring Boot application for managing user tasks.  
Each task has a title, description, due date, status and assignee.  
Tasks can depend on another task: you cannot mark a task as completed if its dependency is not yet completed.  
Data is persisted in-memory (H2) or in MariaDB/PostgreSQL (Docker).  
Includes unit and integration tests.

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
