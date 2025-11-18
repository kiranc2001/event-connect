# Event Connect Backend

# Website

**Repo**: https://github.com/kiranc2001/event-connect

**Postman Collection**: https://github.com/kiranc2001/event-connect/blob/main/Event-connect.postman_collection.json

**Screenshots**: https://github.com/kiranc2001/event-connect/tree/main/screenshots

**For more detailed explaination**:

**Medium**: https://medium.com/@kirangowda0212/building-event-connect-a-robust-spring-boot-backend-for-community-event-management-f2c508c74e66



Event Connect is a full-featured backend for a community web app that allows users to create, discover, and join local events. Built with Spring Boot, it handles CRUD operations for events, user authentication (session-based), participation management, real-time chat (WebSocket), email notifications, and admin analytics. Designed for scalability and security, it integrates PostgreSQL for data persistence.

This is the **backend-only** implementation. For a complete app, pair it with a React frontend (not included here).

## Features

- **User Authentication**: Signup, login, logout, password reset via OTP email.
- **Event Management**: Create, read, update, delete events with validation.
- **Event Discovery**: Search, filter by category/date, list upcoming events.
- **Participation**: Join/leave events with email reminders (scheduled).
- **Real-time Chat**: WebSocket-based chat per event with message persistence.
- **Admin Dashboard**: Analytics endpoints (total users/events, upcoming count) – integrate Chart.js in frontend.
- **Email Service**: OTP and event reminders via Spring Mail (Gmail SMTP).
- **Error Handling**: Custom exceptions and meaningful JSON responses.
- **Security**: Session-based auth (no JWT), BCrypt passwords, role-based (USER/ADMIN).
- **Database**: PostgreSQL with JPA/Hibernate for ORM.

## Tech Stack

- **Backend**: Spring Boot 3.2.x, Java 21
- **Database**: PostgreSQL
- **Validation**: Hibernate Validator
- **Security**: Spring Security (BCrypt encoder)
- **Email**: Spring Mail
- **Chat**: Spring WebSocket with STOMP
- **Testing**: JUnit (basic unit tests)
- **Tools**: IntelliJ IDEA, Maven, pgAdmin/MySQL Workbench (for DB)

## Prerequisites

- Java 21 JDK (e.g., OpenJDK or Oracle)
- PostgreSQL 13+ (install via official site or Homebrew: `brew install postgresql`)
- Maven 3.9+ (bundled with Spring Initializr)
- IntelliJ IDEA (Community or Ultimate)
- Git for version control
- Gmail account for email testing (generate App Password for SMTP)

## Installation

1. **Clone the Repo**:
   ```
   git clone https://github.com/yourusername/event-connect-backend.git
   cd event-connect-backend
   ```

2. **Database Setup**:
   - Start PostgreSQL server (`brew services start postgresql` on Mac).
   - Create database: `createdb event_connect` (or via pgAdmin: New Database → event_connect).
   - Update `src/main/resources/application.properties` with your DB credentials.

3. **Dependencies**:
   - Open in IntelliJ → Maven panel → Reload (or `./mvnw clean install`).

## Configuration

Edit `src/main/resources/application.properties`:
```properties
# Server
server.port=9000

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/event_connect
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Email (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-gmail@gmail.com
spring.mail.password=your-app-password  # Generate at myaccount.google.com/apppasswords

# Session
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
```

- **App Password**: Enable 2FA on Gmail → Google Account > Security > App Passwords > Generate for "Mail".

## Running the App

1. **Start Backend**:
   - IntelliJ: Open `EventConnectBackendApplication.java` → Run (green play button).
   - Or CLI: `./mvnw spring-boot:run`.
   - App runs on `http://localhost:9000`. Check console for "Started EventConnectBackendApplication".

2. **Auto-Migration**: Hibernate creates tables on first run (users, events, participants, messages).

3. **Test API** (Postman/Insomnia):
   - Base URL: `http://localhost:9000`.
   - Signup: POST `/api/users/signup` with JSON body.
   - Login: POST `/api/users/login` → Copy JSESSIONID cookie for auth'd requests.
  .

4. **Verify**:
   - Logs: Watch IntelliJ console for SQL queries/emails.
   - DB: Use pgAdmin to inspect tables.

## API Endpoints

All endpoints return JSON. Auth'd ones require JSESSIONID cookie from login.

| Method | Endpoint | Description | Auth | Body/Example |
|--------|----------|-------------|------|--------------|
| POST | `/api/users/signup` | Create user | No | `{ "name": "John", "email": "john@example.com", "password": "pass123" }` → UserResponseDto |
| POST | `/api/users/login` | Login (sets session) | No | `{ "email": "john@example.com", "password": "pass123" }` → UserResponseDto + Cookie |
| POST | `/api/users/logout` | Logout | Yes | None → `"Logged out successfully"` |
| POST | `/api/users/forgot-password` | Send OTP | No | `{ "email": "john@example.com" }` → `"OTP sent"` |
| PUT | `/api/users/reset-password/{email}` | Reset password | No | `{ "otp": "123456", "newPassword": "newpass" }` → UserResponseDto |
| GET | `/api/users/me` | Current user | Yes | None → UserResponseDto |
| GET | `/api/events` | All events | No | None → Event[] |
| GET | `/api/events/upcoming` | Upcoming events | No | None → Event[] |
| GET | `/api/events/search?q=query` | Search events | No | None → Event[] |
| GET | `/api/events/category/{cat}` | By category | No | None → Event[] |
| POST | `/api/events` | Create event | Yes | EventDto → Event |
| GET | `/api/events/{id}` | Event details | No | None → Event |
| PUT | `/api/events/{id}` | Update event | Yes (creator) | EventDto → Event |
| DELETE | `/api/events/{id}` | Delete event | Yes (creator) | None → `"Event deleted"` |
| GET | `/api/events/my` | User's events | Yes | None → Event[] |
| POST | `/api/participations/join` | Join event | Yes | `{ "eventId": 1 }` → `"Joined"` |
| POST | `/api/participations/leave` | Leave event | Yes | `{ "eventId": 1 }` → `"Left"` |
| GET | `/api/participations/joined` | Joined events | Yes | None → Event[] |
| GET | `/api/chat/messages/{id}` | Chat history | No | None → Message[] |
| POST | `/api/chat/send/{id}` | Send message | Yes | `{ "message": "Hi" }` → `"Sent"` (WebSocket for real-time) |
| GET | `/api/admin/analytics` | Stats | No (add auth) | None → `{ "totalUsers": 5, ... }` |

**Error Responses**: 400/404/500 with JSON `{ "error": "Message" }` or field-specific `{ "email": "Invalid" }`.

## Database Schema

Auto-created by Hibernate:
- `users`: id, name, email, password_hash, role, otp_code, otp_expiry, created_at
- `events`: id, title, description, category, date, location, created_by (FK), created_at
- `participants`: id, user_id (FK), event_id (FK), joined_at
- `messages`: id, event_id (FK), user_id (FK), message, timestamp

## Email & Scheduling

- **OTP/Reminders**: Configured for Gmail. Test with Mailtrap.io for dev.
- **Batch Reminders**: Scheduled task sends emails 1h before events (every minute check).

## Testing

- **Unit Tests**: Add in `src/test/java` (e.g., `@SpringBootTest` for services).
- **API**: Use Postman collection (import JSON with endpoints above).
- **Load Test**: JMeter for concurrent joins/chats.


## Contributing

1. Fork the repo.
2. Create branch: `git checkout -b feature-branch`.
3. Commit: `git commit -m "Add feature"`.
4. Push: `git push origin feature-branch`.


## Contact

Email – your.email@example.com

LinkedIn - https://www.linkedin.com/in/kiran-c-gowda-2507021b9/



*Built for local communities. Star the repo if useful!*
