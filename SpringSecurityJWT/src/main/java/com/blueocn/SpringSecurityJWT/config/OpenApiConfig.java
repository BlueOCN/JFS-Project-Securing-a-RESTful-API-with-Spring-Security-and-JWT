package com.blueocn.SpringSecurityJWT.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "BlueOCN",
                        email = "BlueOCN@email.com",
                        url = "https://github.com/BlueOCN"
                ),
                description = "This project involves developing a secured RESTful API for a User Management System using Spring Boot and Spring Security. It enables user registration, authentication with JWT tokens, and role-based access control. The API also supports essential user operations, including viewing profiles, updating information, and account deletion.",
                title = "Securing a RESTful API with Spring Security and JWT",
                version = "1.0",
                license = @License(
                        name = "MIT",
                        url = "https://github.com/BlueOCN/JFS-Project-Securing-a-RESTful-API-with-Spring-Security-and-JWT?tab=MIT-1-ov-file"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using Bearer token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@SecurityScheme(
        name = "basicAuth",
        description = "Basic authentication with username and password",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
