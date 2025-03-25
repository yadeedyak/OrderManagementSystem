Order Management System

Overview

The Order Management System is a backend service built using Spring Boot that provides functionality for managing users, items, orders, and sellers. It implements Spring Security with JWT authentication and utilizes Redis caching to optimize performance.

Features

User Management: Register, update, and delete users (both customers and sellers).

Authentication & Authorization: JWT-based security with role-based access control.

Item Management: Sellers can create, update, list, and delete items.

Order Processing: Customers can create and modify orders, and sellers can manage their orders.

Caching: Redis is used to cache items for improved performance.

Automated Order Deletion: Orders expire automatically after 1.5 hours using a scheduled task.

Tech Stack

Spring Boot (Backend Framework)

Spring Security & JWT (Authentication & Authorization)

Spring Data JPA (Database Integration)

MySQL (Relational Database)

Redis (Caching Layer)

Lombok (Code Simplification)

Scheduled Jobs (Automatic Order Expiry)

Installation & Setup

Prerequisites

Java 17+

Maven

MySQL

Redis

Steps to Run

Clone the repository:

git clone https://github.com/yadeedyak/OrderManagementSystem.git
cd OrderManagementSystem

Configure the database in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/order_db
spring.datasource.username=root
spring.datasource.password=yourpassword

Install dependencies and build the project:

mvn clean install

Run the application:

mvn spring-boot:run

Redis should be running on localhost:6379 for caching to work.

API Endpoints

Authentication

POST /auth/register – Register a new user

POST /auth/login – Authenticate and receive a JWT token

User Management

GET /users/{username} – Get user details

PUT /users/{username} – Update user details

DELETE /users/{username} – Delete a user

Item Management (Seller Only)

POST /items – Create an item

PUT /items – Update an item

GET /items/{username} – Get seller's items

DELETE /items/{itemId} – Delete an item

Order Management

POST /orders – Create an order

GET /orders/{username} – Get all customer orders

PUT /orders/{itemId} – Update an order

DELETE /orders/{orderId} – Delete an order

Security & Authentication

Uses JWT for securing endpoints.

Roles: USER, SELLER.

Protected endpoints require an Authorization: Bearer <token> header.

Caching with Redis

Items are cached in Redis to reduce database queries.

Redis expiration time: 10 minutes.

Automated Order Deletion

A scheduled job runs every 1.5 hours to automatically delete expired orders from the database.

Contributors

Yadeedya (Developer)
