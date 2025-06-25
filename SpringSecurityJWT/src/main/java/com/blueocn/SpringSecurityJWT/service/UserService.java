package com.blueocn.SpringSecurityJWT.service;

import com.blueocn.SpringSecurityJWT.data.dto.AuthRequest;
import com.blueocn.SpringSecurityJWT.data.dto.RegisterRequest;
import com.blueocn.SpringSecurityJWT.data.dto.UpdateRequest;
import com.blueocn.SpringSecurityJWT.data.entity.user.UserEntity;
import com.blueocn.SpringSecurityJWT.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getUsers() {
        LOGGER.debug("Fetching all users");
        List<UserEntity> users = userRepository.findAll();
        LOGGER.info("Retrieved {} users from the database", users.size());
        return users;
    }

    public UserEntity registerUser(RegisterRequest request) {
        LOGGER.debug("Attempting to register user: {}", request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            LOGGER.warn("Registration failed - username '{}' is already taken", request.getUsername());
            throw new IllegalArgumentException("Username is already taken.");
        }

        // Create User record
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        user.setEnabled(true);
        UserEntity savedUser = userRepository.save(user);
        LOGGER.info("User '{}' successfully registered", savedUser.getUsername());
        return savedUser;
    }

    public UserEntity updateUser(String username, UpdateRequest request) {
        LOGGER.debug("Attempting to update user: {}", username);
        return userRepository.findByUsername(username)
                .map(userEntity -> {
                    userEntity.setUsername(request.getUsername());
                    userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
                    userEntity.setEnabled(request.getEnabled());

                    UserEntity updatedUser = userRepository.save(userEntity);
                    LOGGER.info("User '{}' successfully updated", updatedUser.getUsername());
                    return updatedUser;
                }).orElseThrow(() -> {
                    LOGGER.error("User '{}' not found for update", username);
                    return new RuntimeException("User not found");
                });
    }

    public void deleteUser(String username) {
        LOGGER.debug("Attempting to delete user: {}", username);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOGGER.error("User '{}' not found for deletion", username);
                    return new IllegalArgumentException("User not found");
                });
        userRepository.delete(user);
        LOGGER.info("User '{}' successfully deleted", username);
    }

    public UserEntity getUserByUsername(String username) {
        LOGGER.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOGGER.warn("User '{}' not found", username);
                    return new IllegalArgumentException("User not found");
                });

    }

    public ResponseEntity<String> verify(AuthRequest request) {

        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(jwtService.generateToken(userDetails));
        }

        return ResponseEntity.badRequest().body("Failed");
    }
}