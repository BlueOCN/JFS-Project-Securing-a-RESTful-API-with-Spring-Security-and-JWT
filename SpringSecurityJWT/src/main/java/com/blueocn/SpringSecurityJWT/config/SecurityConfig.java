package com.blueocn.SpringSecurityJWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Configures Spring Security for the application.
 * Includes password encryption.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines a BCrypt password encoder bean.
     * This ensures passwords are securely hashed before storage.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
