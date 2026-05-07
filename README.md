# Parkiyo - Vehicle Parking Management System

Parkiyo is a Spring Boot and Thymeleaf parking management SaaS application with admin/user dashboards, vehicle registration, slot management, entry/exit processing, reservations, wallet payments, receipts, notifications, audit logs, and reports.

## Requirements

- Java 17 or newer
- MySQL 8 or newer

## Run Locally

1. Create or allow the app to create the database:
   - The default JDBC URL includes `createDatabaseIfNotExist=true`.
   - Reference SQL is available at `src/main/resources/database/schema.sql`.
2. Configure credentials with environment variables, or edit `src/main/resources/application.properties`:
   - `PARKIYO_DB_URL`
   - `PARKIYO_DB_USERNAME`
   - `PARKIYO_DB_PASSWORD`
3. Start the app:
   - Windows: `.\mvnw.cmd spring-boot:run`
   - macOS/Linux: `./mvnw spring-boot:run`
4. Open `http://localhost:8080`.

## First Login

The backend creates a first admin user and starter parking slots on first run when `parkiyo.bootstrap.enabled=true`.

- Admin email: `admin@parkiyo.com`
- Admin password: `Admin@12345`

Override these before deployment:

- `PARKIYO_ADMIN_EMAIL`
- `PARKIYO_ADMIN_PASSWORD`
- `PARKIYO_BOOTSTRAP_ENABLED=false`
- `PARKIYO_DEMO_SLOTS_ENABLED=false`

## Database Files

- `src/main/resources/database/schema.sql` is the clean MySQL 8 reference schema.
- `src/main/resources/database/seed.sql` is optional demo data.
- Runtime startup does not execute these SQL files automatically. The app uses `spring.jpa.hibernate.ddl-auto=update` to keep startup non-destructive.

## Tests

Run:

```powershell
.\mvnw.cmd test
```

Tests use H2 through `src/test/resources/application-test.properties`, so they do not require MySQL.
