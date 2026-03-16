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
User Management

    POST /user/create - Register a new user
        Request body: UserCreateRequestDto
        Response: UserResponseDto

    GET /user/by-email - Get user by email
        Query param: email
        Response: UserResponseDto
    
    POST /user/login - login user and give token
        Request body: RequestLoginDto
        Response: LoginResponseDto

Authentication

    Endpoints are protected with JWT tokens
    Include token in Authorization header: Bearer <token>
