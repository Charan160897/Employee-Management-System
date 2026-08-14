# Employee Management System

A full-stack Employee Management System built using **Spring Boot**, **React**, **MySQL**, and **JWT Authentication**. The application provides secure employee management with role-based access control, advanced search, pagination, employee profiles, dashboard analytics, and audit logging.

---

# Features

## Authentication & Authorization

- JWT-based authentication
- Secure login and registration
- Role-based access control (ADMIN / USER)
- Protected API endpoints
- Protected React routes
- Automatic logout on unauthorized access

---

## Employee Management

- Create Employee
- View Employee
- Update Employee
- Delete Employee
- Employee Profile View
- Employee Status (Active/Inactive)
- Duplicate email validation
- Form validation

---

## Employee Dashboard

- Total Employees
- Active Employees
- Inactive Employees
- Average Salary
- Total Departments
- Department Summary

---

## Search & Filters

- Search employees by keyword
- Filter by department
- Filter by status
- Clear filters
- Active filter summary

---

## Sorting & Pagination

- Sort by
  - ID
  - First Name
  - Last Name
  - Department
  - Job Title
  - Salary
  - Hire Date

- Ascending / Descending sorting

- Pagination

- Rows per page
  - 5
  - 10
  - 20

---

## Employee Profile

- View employee details
- Personal Information
- Employment Information
- Copy email
- Edit employee
- Close profile

---

## Audit Logging

Tracks every employee operation.

Supported actions:

- CREATE
- UPDATE
- DELETE

Each audit record stores:

- Username
- Action
- Employee Name
- Timestamp

Only ADMIN users can access audit logs.

---

## Backend Features

- Spring Boot REST APIs
- Layered Architecture
- DTO Pattern
- Entity Mapping
- Repository Pattern
- Service Layer
- Mapper Layer
- Global Exception Handling
- Validation
- Audit Logging
- Spring Security
- JWT Authentication
- Role-Based Authorization

---

## Frontend Features

- React + Vite
- Axios
- Protected Routes
- Login Page
- Dashboard
- Employee CRUD
- Employee Profile Modal
- Dashboard Cards
- Audit Log Page
- Loading States
- Error Handling
- Responsive Layout

---

# Tech Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- Maven
- MySQL
- JUnit 5
- Mockito

---

## Frontend

- React
- Vite
- JavaScript
- Axios
- CSS3

---

## Database

- MySQL

---

# Project Structure

## Backend

```
src
 ├── controller
 ├── service
 │     └── impl
 ├── repository
 ├── entity
 ├── dto
 ├── mapper
 ├── security
 ├── config
 ├── exception
 └── util
```

---

## Frontend

```
src
 ├── api
 ├── components
 ├── hooks
 ├── assets
 ├── App.jsx
 └── main.jsx
```

---

# REST APIs

## Authentication

| Method | Endpoint |
|----------|-------------------------|
| POST | /api/auth/register |
| POST | /api/auth/login |

---

## Employees

| Method | Endpoint |
|----------|-------------------------|
| GET | /api/employees |
| GET | /api/employees/{id} |
| POST | /api/employees |
| PUT | /api/employees/{id} |
| DELETE | /api/employees/{id} |

---

## Dashboard

| Method | Endpoint |
|----------|-------------------------|
| GET | /api/employees/dashboard |

---

## Audit Logs

| Method | Endpoint |
|----------|-------------------------|
| GET | /api/audit |

---

# Security

| Role | Permissions |
|--------|--------------------------------|
| ADMIN | Full CRUD + Audit Logs |
| USER | View Employees Only |

---

# Validation

Employee validations include:

- First Name Required
- Last Name Required
- Valid Email
- Unique Email
- Department Required
- Job Title Required
- Salary Required
- Hire Date Required

---

# Dashboard Statistics

The dashboard displays:

- Total Employees
- Active Employees
- Inactive Employees
- Average Salary
- Total Departments
- Department Employee Count

---

# Audit Logging

Every employee modification is recorded.

Example:

| Username | Action | Employee | Timestamp |
|----------|---------|----------|----------------|
| admin | CREATE | Rahul Kumar | 2026-08-14 10:15 |
| admin | UPDATE | John Smith | 2026-08-14 10:35 |
| admin | DELETE | Priya Patel | 2026-08-14 10:50 |

---

# Testing

## Backend

- JUnit 5
- Mockito

Tests include:

- Employee Service
- Employee Controller
- Authentication
- Audit Service

---

## Frontend

- Vitest
- React Testing Library

Tests include:

- Employee List
- Employee Profile
- Authentication
- Audit Log Table

---

# Error Handling

Implemented using global exception handling.

Examples:

- Employee Not Found
- Duplicate Email
- Invalid Credentials
- Unauthorized Access
- Forbidden Access
- Validation Errors

---

# Authentication Flow

```
User Login
      │
      ▼
JWT Generated
      │
      ▼
React Stores Token
      │
      ▼
Axios Interceptor
      │
      ▼
Authorization Header
      │
      ▼
Spring Security
      │
      ▼
Protected APIs
```

---

# Audit Flow

```
Create / Update / Delete Employee
              │
              ▼
EmployeeService
              │
              ▼
AuditLogService
              │
              ▼
AuditLogRepository
              │
              ▼
MySQL
              │
              ▼
AuditLogController
              │
              ▼
React Audit Logs Page
```

---

# Current Project Status

## Completed (Day 1 – Day 14)

- Spring Boot Setup
- React Setup
- MySQL Integration
- Employee CRUD
- DTO Mapping
- Repository Layer
- Service Layer
- REST APIs
- Validation
- Exception Handling
- Search
- Filters
- Pagination
- Sorting
- Dashboard Statistics
- JWT Authentication
- Spring Security
- Role-Based Authorization
- Employee Profile
- Audit Logging
- Backend Unit Testing
- Frontend Component Testing

---

# Upcoming Features

- Reports & Analytics
- Charts
- CSV Export
- Employee Photo Upload
- Resume Upload
- Email Notifications
- Docker
- Cloud Deployment
- Swagger Documentation
- Project Documentation

---

# Author

**Sai Charan Reddy Pandiri**

Full Stack Developer

Tech Stack:

- Java
- Spring Boot
- Spring Security
- React
- MySQL
- REST APIs
- JWT
- JPA/Hibernate
- Maven
- Git