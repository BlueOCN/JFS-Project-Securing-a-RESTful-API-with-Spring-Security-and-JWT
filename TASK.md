# Project: Securing a RESTful API with Spring Security and JWT
You are tasked with building a **secured RESTful API** for a **User Management System** using Spring Boot and Spring Security. The API will:

- Allow users to register and log in to the system.
- Issue **JWT tokens** upon successful login.
- Secure endpoints so that only authorized users can access them based on their roles.
- Include basic user operations such as viewing user details, updating user information, and deleting accounts.

## Project Objectives
By the end of this project, you should be able to:

- Set up Spring Security to secure a RESTful API.
- Implement user authentication and authorization.
- Use **JWT (JSON Web Tokens)** for stateless authentication.
- Secure API endpoints based on user roles (e.g., admin, user).
- Protect sensitive data by configuring access control rules.

## Learning Outcomes
By completing this project, you will:

- Understand how to integrate Spring Security with Spring Boot.
- Learn to use JWT for stateless authentication.
- Gain experience in securing API endpoints based on roles.
- Learn to hash passwords securely using BCrypt.
- Develop skills in building a secure backend system for a real-world use case.

## Instructions

This guide walks through setting up a Spring Boot project with Spring Security and JWT authentication. It begins with generating the project using Spring Initializer and configuring a MySQL database. Next, it defines a User entity with role-based access control, ensuring secure authentication.

Password security is enforced using BCrypt hashing, followed by implementing user registration and login endpoints. JWT is configured for stateless authentication, allowing secure token-based access. API endpoints are protected using role-based access control, restricting admin operations.

Finally, testing is conducted using Postman or cURL, verifying authentication and access control. Additional enhancements include password reset, rate limiting, and multi-factor authentication to strengthen security. This structured approach ensures a scalable and secure backend system.

### Step 1: Set Up the Project

1. Use Spring Initializer to generate a new Spring Boot project.
   - **Dependencies:** Spring Web, Spring Security, Spring Data JPA, MySQL Driver, and JSON Web Token libraries (e.g., `jjwt`).
2. Import the project into your IDE (e.g., IntelliJ, Eclipse).

---

### Step 2: Configure MySQL Database

1. Set up a MySQL database locally or on a cloud service.
2. Configure the database connection in the `application.properties` file with properties like:
   - Database URL, username, password.
   - Hibernate's `ddl-auto` to automatically create and update tables.

---

### Step 3: Implement User Entity and Role-Based Access

1. Create a `User` entity with fields like:
   - `id`: Unique identifier.
   - `username`: Login username.
   - `password`: Encrypted password.
   - `role`: Role of the user (e.g., ADMIN, USER).
2. Use Hibernate to map the `User` entity to a database table.
3. Store roles in the database and associate them with users.

---

### Step 4: Secure Passwords

1. Use **BCrypt** to hash user passwords before storing them in the database. 
2. Configure Spring Security to use the hashed passwords for authentication.

---

### Step 5: Implement User Registration and Login

1. Create endpoints for:
   - **User Registration:** Save a new user with a hashed password in the database.
   - **User Login:** Authenticate users using their username and password.
   - Generate a JWT token upon successful authentication.
2. Use Spring Security's `AuthenticationManager` to validate login credentials.

---

### Step 6: Configure JWT for Stateless Authentication

1. Use a JWT library to generate and validate tokens.
2. Implement a custom security filter to:
   - Extract the JWT token from the `Authorization` header.
   - Validate the token and set the security context for authorized requests.
3. Ensure the API remains stateless by not maintaining sessions on the server.

---

### Step 7: Secure API Endpoints

1. Use role-based access control to restrict certain endpoints:
   - Allow **all users** to access registration and login endpoints.
   - Restrict **admin-only operations** (e.g., deleting accounts) to users with the ADMIN role.
   - Allow users to access their profile but not profiles of other users.
2. Use annotations like `@PreAuthorize` or configure access control in the Spring Security configuration class.

---

### Step 8: Test the Application

1. Use **Postman** or **cURL** to test the following workflows:
   - Register a new user.
   - Log in with the registered user and retrieve a JWT token.
   - Access protected endpoints using the token in the `Authorization` header.
   - Verify access control for different user roles.
2. Check the database to ensure that users and roles are stored correctly.

---

### Additional Challenges
To enhance the project, you can:

- Implement password reset functionality.
- Add rate limiting to prevent brute-force attacks.
- Use Swagger/OpenAPI to document the secured API endpoints.
- Implement multi-factor authentication (e.g., email or OTP verification).