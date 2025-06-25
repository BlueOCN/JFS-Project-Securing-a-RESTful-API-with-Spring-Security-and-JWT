# Securing a RESTful API with Spring Security and JWT

This project is a secured RESTful API for a **User Management System** developed using **Spring Boot** and **Spring Security**. It supports essential user operations such as registration, authentication using JWT tokens, and role-based access control. The API also provides capabilities for users to view their profile, update their information, and delete their account.

## 🛠 Technology Stack

- Java
- Spring Boot
- Spring Security
- JSON Web Tokens (JWT)
- REST API
- Swagger UI (OpenAPI 3.1)

## 🚀 Project Setup
Follow these steps to clone and configure the project

### ✏️ Clone the repository

```shell
   git https://github.com/BlueOCN/JFS-Project-Securing-a-RESTful-API-with-Spring-Security-and-JWT.git
   cd SpringSecurityJWT
 ```

---

### 🔍 Configure the database

Import the Library Management database to your (MySQL) database instance
```shell
  cmd.exe /c "mysql -u root -p usermanagement2 < ./DB/DB_SQL_dump.sql"
```

Update `src/main/resources/application.properties` with your database details.
```
spring.datasource.url=jdbc:mysql://localhost:xxxx/usermanagement2
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 🛠️ Build the project
```shell
  mvn clean install
```

---

### ▶️ Running the Application
```shell
  mvn spring-boot:run
```

## 📌 API Endpoints

### User Controller

| Method | Endpoint             | Description                    |
|--------|----------------------|--------------------------------|
| POST   | `/users/register`    | Register a new user            |
| POST   | `/users/login`       | Authenticate user (returns JWT)|
| GET    | `/users`             | Retrieve all users             |
| GET    | `/users/me`          | Get current authenticated user |
| PUT    | `/users/{username}`  | Update user by username        |
| DELETE | `/users/{username}`  | Delete user by username        |

## 📝 POST `/users/register`

Registers a new user account into the system with provided credentials and authority.

### 🔐 Authorization

- No authentication required. This endpoint is public to allow new user registration.

---

### ✅ Request

**Method:**
```txt
POST /users/register
```

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "username": "user2",
  "password": "abcd1234",
  "authority": "ROLE_USER"
}
```

| Field     | Type   | Required | Description                         |
|-----------|--------|----------|-------------------------------------|
| username  | string | Yes      | Desired unique username             |
| password  | string | Yes      | Password (will be hashed internally)|
| authority | string | Yes      | Role assigned (e.g. `ROLE_USER`)    |

**Example cURL:**
```shell
curl -X 'POST' \
'http://localhost:8080/users/register' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"username": "user2",
"password": "abcd1234",
"authority": "ROLE_USER"
}'
```

---

### 📦 Response

- **201 Created**  
  Indicates successful registration.

```
User registered successfully.
```

---

### 🛡 Access Control

| Access Type          | Value     |
|----------------------|-----------|
| Requires JWT Token   | ❌ No     |
| Accessible to Public | ✅ Yes    |

## 🔐 POST `/users/login`

Authenticates a user by verifying the provided credentials and returns a **JWT token** upon successful authentication. This token is used for accessing protected resources in subsequent requests.

### 🔐 Authorization

- No prior authentication required to access this endpoint.

---

### ✅ Request

**Method:**
```txt
POST /users/login
```

**Content-Type:** `application/json`

**Request Body Example:**
```json
{
  "username": "user2",
  "password": "abcd1234"
}
```

| Field     | Type   | Required | Description                    |
|-----------|--------|----------|--------------------------------|
| username  | string | Yes      | The username of the user       |
| password  | string | Yes      | The raw password for login     |

**Example cURL:**
```shell
curl -X 'POST' \
'http://localhost:8080/users/login' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"username": "user2",
"password": "abcd1234"
}'
```

---

### 📦 Response

- **200 OK**  
  Returns a JWT token string in the response body upon successful login.

**Example Response:**
```txt
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImlhdCI6MTY5ODQyMzM1NywiZXhwIjoxNjk4NDI2OTU3fQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
```

---

### 🛡 Access Control

| Access Type        | Value     |
|--------------------|-----------|
| Requires JWT Token | ❌ No     |
| Accessible to All  | ✅ Yes    |


## 📥 GET `/users`

Retrieves a list of all registered users in the system. This endpoint is secured and intended for users with administrative privileges.

### 🔐 Authorization
Requires a valid JWT token with role `ADMIN`.

**Header:**
```txt
Authorization: Bearer <your-jwt-token>
```

---

### ✅ Request

**Method:**  
```txt
GET /users
```

**Example cURL:**
```shell
curl -X 'GET' \
'http://localhost:8080/users' \
-H 'accept: */*' \
-H 'Authorization: Bearer <your-jwt-token>'
```

---

### 📦 Response

**Status Code:** `200 OK`  
**Media Type:** `application/json`

Returns a JSON array of user objects with the following fields:

```json
[
  {
    "id": 8,
    "username": "user1",
    "password": "$2a$10$q5Jt1wi4wJMqG3JFzB3RBuvlH8F308lztF7jRzkDFUKMPC1ouKQjy",
    "enabled": true
  },
  {
    "id": 9,
    "username": "admin",
    "password": "$2a$10$rv1ds6qkwSaQyGk761C5leSKZP.A6p8Nlg.BrfUldJGVwm0qznT/a",
    "enabled": true
  },
  {
    "id": 14,
    "username": "user2",
    "password": "$2a$10$T.jcyfENTJ6mpX4FVLafIuyP1RJzvizxk5AVl3baOkXZ27R7I4RyK",
    "enabled": true
  }
]
```

---

### 🛡 Access Control

| Role   | Access |
|--------|--------|
| USER   | ❌ Denied |
| ADMIN  | ✅ Allowed |

## 📥 GET `/users/me`

Retrieves the authenticated user's profile based on the JWT token provided in the request.

### 🔐 Authorization

Requires a valid JWT token in the `Authorization` header.

**Header:**
```txt
Authorization: Bearer <your-jwt-token>
```

---

### ✅ Request

**Method:**
```txt
GET /users/me
```

**Example cURL:**
```shell
curl -X 'GET' \
'http://localhost:8080/users/me' \
-H 'accept: */*' \
-H 'Authorization: Bearer <your-jwt-token>'
```

---

### 📦 Response

**Status Code:** `200 OK`  
**Media Type:** `application/json`

Returns a JSON object representing the authenticated user.

```json
{
  "id": 9,
  "username": "admin",
  "password": "$2a$10$rv1ds6qkwSaQyGk761C5leSKZP.A6p8Nlg.BrfUldJGVwm0qznT/a",
  "enabled": true
}
```

---

### 🛡 Access Control

| Requirement        | Value           |
|--------------------|-----------------|
| Authentication     | ✅ Required     |
| Authorization Role | Any authenticated user |

## 🗑️ DELETE `/users/{username}`

Deletes a user account identified by the `username` path parameter. This action is protected and typically limited to users with administrative privileges.

### 🔐 Authorization

Requires a valid JWT token with role `ADMIN`.

**Header:**
```txt
Authorization: Bearer <your-jwt-token>
```

---

### ✅ Request

**Method:**
```txt
DELETE /users/{username}
```

**Path Parameter:**

| Name      | Type   | Required | Description               |
|-----------|--------|----------|---------------------------|
| username  | string | Yes      | Unique username to delete |

**Example:**
```txt
DELETE /users/user2
```

**Example cURL:**
```shell
curl -X 'DELETE' \
'http://localhost:8080/users/user2' \
-H 'accept: */*' \
-H 'Authorization: Bearer <your-jwt-token>'
```

---

### 📦 Response

- **204 No Content**  
  Indicates successful deletion. The response contains no body.

---

### 🛡 Access Control

| Role   | Access     |
|--------|------------|
| USER   | ❌ Denied   |
| ADMIN  | ✅ Allowed  |



## 📁 Data Schemas

### 🔸 RegisterRequest

| Field    | Type   | Required | Description                     |
|----------|--------|----------|---------------------------------|
| username | string | Yes      | Must be unique and non-blank    |
| password | string | Yes      | Must meet password criteria     |
| email    | string | Yes      | Valid email format required     |
| role     | string | Yes      | Should match predefined roles   |

### 🔸 AuthRequest

| Field    | Type   | Required | Description                   |
|----------|--------|----------|-------------------------------|
| username | string | Yes      | Registered username           |
| password | string | Yes      | Corresponding account password|

### 🔸 UpdateRequest

| Field    | Type   | Required | Description                             |
|----------|--------|----------|-----------------------------------------|
| email    | string | No       | Updated email (if changed)              |
| password | string | No       | New password (must meet security rules) |
| role     | string | No       | Role to assign (admin/user)             |

### 🔸 UserEntity

| Field     | Type      | Description                       |
|-----------|-----------|-----------------------------------|
| id        | long      | Auto-generated unique identifier  |
| username  | string    | Unique, non-null                  |
| password  | string    | Hashed using BCrypt               |
| enabled   | boolean   | non-null                          | 

## 🔐 Security

- **Stateless authentication** using **JWT tokens**
- **Role-based access control** for endpoint protection
- **Secure password storage** using BCrypt hashing algorithm
- **Spring Security 6.5** configuration with `SecurityFilterChain`

## 🌐 Environment

- Local server: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html#/user-controller`

## 📄 Terms of Service

- **Author**: BlueOCN
- **Website**: BlueOCN - Website
- **License**: MIT
- **Contact**: [Send email to BlueOCN]

---
