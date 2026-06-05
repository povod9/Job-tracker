# Job Tracker

A Spring Boot application for tracking job applications, reminders, and activity events.

## Overview

Job Tracker is a backend service that helps users manage their job search process by tracking applications, setting reminders, logging activity events, and integrating with Telegram for notifications.

## Tech Stack

- **Framework**: Spring Boot 4.0.3
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Hibernate
- **Authentication**: JWT
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Migration**: Flyway 
- **Build Tool**: Maven
- **Mapping**: MapStruct

## Running with Docker

- docker-compose up -d

This starts a PostgreSQL database on port 5432.

## API Endpoints

### 1. User Controller (`/user`)
* **POST** `/user/register` — Register a new standard user.
* **POST** `/user/login` — Authenticate user and receive a JWT token.
* **PUT** `/user/me` — Update current user profile data.

### 2. Vacancy Controller (`/vacancy`)
* **POST** `/vacancy/me` — Create a new vacancy (HR access).
* **GET** `/vacancy/me/vacancies` — Get all vacancies created by the current HR (supports optional `status` query param).
* **GET** `/vacancy/me/{id}` — Get a specific vacancy by its ID.
* **DELETE** `/vacancy/me/{id}` — Delete a vacancy by its ID.
* **PUT** `/vacancy/me/{id}` - Update a vacancy by its ID.

### 3. Application Controller (`/application`)
* **POST** `/application/me` — Create a new job application entry.
* **GET** `/application/me/applications` — Get a list of all job applications belonging to the current user.
* **PUT** `/application/me/status/{id}` — Update the status of a specific application by ID (requires `applicationStatus` query param).
* **DELETE** `/application/me/{id}` — Delete a job application by its ID.

### 4. Reminder Controller (`/reminder`)
* **POST** `/reminder/me/{id}` — Create a reminder associated with a specific application ID.
* **GET** `/reminder/me/reminders` — Get all reminders for the current user.

### 5. HR Controller (`/hr`)
* **POST** `/hr/register` — Register a new HR manager account.

### 6. Admin Controller (`/admin`)
* **POST** `/admin/register` — Register a new administrator account.
* **GET** `/admin/users` — Get a list of all registered users in the system.
* **GET** `/admin/users/email/{email}` — Find a specific user by their email address.
* **DELETE** `/admin/users/email/{email}` — Delete a user from the system by their email address.
* **GET** `/admin/applications` — Get a list of all deleted applications in the system.
* **GET** `/admin/activities` — Get a global log stream of all user activity events.

### 7. Swagger
```
http://localhost:8080/swagger-ui.html
```
### Authentication

    Endpoints are protected with JWT tokens
    Include token in Authorization header: Bearer <token>
