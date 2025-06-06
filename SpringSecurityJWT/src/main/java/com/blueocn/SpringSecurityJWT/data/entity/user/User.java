package com.blueocn.SpringSecurityJWT.data.entity.user;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a User entity in the system.
 * Uses Hibernate and Spring Security for persistence and authentication.
 */
@Entity
@Table(name = "users")
public class User {

    // Primary Key: Auto-generated unique ID for each user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Unique and required username field (max length: 50 characters)
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    // Securely stored password (hashed using BCrypt)
    @Column(name = "password", length = 255, unique = false, nullable = false)
    private String password;

    // Enum for user roles (stored as String)
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 10, unique = false, nullable = false)
    private UserRole role;

    // Automatically set timestamp when the user is created (immutable)
    @CreationTimestamp
    @Column(name = "created_at", unique = false, nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Automatically updated timestamp whenever the entity is modified
    @UpdateTimestamp
    @Column(name = "updated_at", unique = false, nullable = false, updatable = true)
    private LocalDateTime updatedAt;

    /**
     * Default constructor required by JPA.
     * Protected to prevent direct instantiation outside Hibernate.
     */
    protected User() {}

    /**
     * Parameterized constructor to create a new User.
     * The password is securely hashed before storing.
     */
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.setPassword(password);
        this.role = role;
    }

    // Getters and setters to maintain encapsulation

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Setter for password with automatic BCrypt hashing.
     * Prevents storing plaintext passwords in the database.
     */
    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Custom equals method to compare users by ID, username, and creation date.
     * Omits password for security reasons.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return id == user.id && Objects.equals(username, user.username) && role == user.role && Objects.equals(createdAt, user.createdAt);
    }

    /**
     * HashCode method based on unique fields (excluding password for security).
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, role, createdAt);
    }

    /**
     * Readable toString method (excluding password to prevent exposure).
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

