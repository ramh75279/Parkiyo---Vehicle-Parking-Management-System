# Parkiyo — Vehicle Parking Management System

## Prerequisites
- Java `17+` (recommended: `17` or `21`)
- MySQL `8+`

## Database setup
1. Create the database:
   - `CREATE DATABASE parkiyo;`
2. Update DB credentials in `src/main/resources/application.properties`:
   - `spring.datasource.username`
   - `spring.datasource.password`
3. Start the app once (tables are created/updated via `spring.jpa.hibernate.ddl-auto=update`).
   - Optional: the reference schema is in `src/main/resources/database/schema.sql`.

## Run
- `./mvnw spring-boot:run`
- App runs on `http://localhost:8080`

## First admin user
- Register a normal user via `/register`, then promote it in MySQL:
  - `UPDATE users SET role='ADMIN' WHERE email='you@example.com';`
