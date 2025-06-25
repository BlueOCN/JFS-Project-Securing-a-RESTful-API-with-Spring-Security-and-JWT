package com.blueocn.SpringSecurityJWT.controller;

import com.blueocn.SpringSecurityJWT.data.dto.AuthRequest;
import com.blueocn.SpringSecurityJWT.data.dto.RegisterRequest;
import com.blueocn.SpringSecurityJWT.data.dto.UpdateRequest;
import com.blueocn.SpringSecurityJWT.data.entity.user.UserEntity;
import com.blueocn.SpringSecurityJWT.service.AuthorityService;
import com.blueocn.SpringSecurityJWT.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final AuthorityService authorityService;

//    @Autowired
//    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    public UserController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        authorityService.registerAuthority(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return userService.verify(request);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @SecurityRequirement(name = "basicAuth")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}")
    @SecurityRequirement(name = "basicAuth")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updateUser(@PathVariable("username") String username, @Valid @RequestBody UpdateRequest request) {
        userService.updateUser(username, request);
        authorityService.updateAuthority(username, request);
        return ResponseEntity.ok("User updated successfully.");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    @SecurityRequirement(name = "basicAuth")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        authorityService.deleteAuthority(username);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @SecurityRequirement(name = "basicAuth")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserEntity> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

}
