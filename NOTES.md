
## Recomdended Project Structure

src/main/java/com/yourpackage/
├── config/              # ✅ Security Configurations
│   ├── SecurityConfig.java         # Main security configuration
│   ├── JwtSecurityConfig.java      # JWT configuration (if using JWT)
│   ├── WebSecurityConfig.java      # Web security filters
│   ├── CustomUserDetailsService.java # User authentication service
│
├── data/              # Database-related entities & repositories
│   ├── entity/        # User, Roles, etc.
│   ├── repository/    # JPA repositories
│
├── service/           # Business logic and user-related services
├── controller/        # REST API controllers
├── utils/             # Utility classes (e.g., token utilities)
