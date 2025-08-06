# Spring Security Microservices Project

This project is a microservices-based system that uses **Spring Boot** and **Spring Security** for securing various services like authentication, authorization, and communication between services using JWT and other techniques.

## 🧩 Microservices Included

- `auth-service` – Handles user authentication and issues JWT tokens.
- `api-gateway` – Entry point for all client requests. Routes requests to services and enforces security.
- `admin-service` – Backend service for admin operations.
- `client-service` – Backend service for client/customer operations.
- `merchant-service` – Backend service for merchant operations.
- `user-service` – Handles user-related operations.
- `eurek-service` – Eureka service registry for service discovery.
- `SecurityDemo` – A sample Spring Boot app demonstrating Spring Security features.

## 🔐 Security Features

- JWT-based Authentication
- Role-based Authorization
- Gateway filtering
- Token validation
- Secure communication between services
- Centralized Authentication via `auth-service`

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Cloud (Eureka, Gateway)
- Maven
- JWT (JSON Web Tokens)
- Eclipse IDE (optional)

## 🛠️ Getting Started

### Prerequisites

- JDK 17+
- Maven
- Git


