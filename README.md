# 💸 Expense Manager — Backend

A production-ready **Spring Boot REST API** for an Enterprise Expense & Budget Management System. It provides secure authentication, full expense tracking, email notifications, and database migrations — all containerized with Docker.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | MySQL |
| Migrations | Flyway |
| ORM | Spring Data JPA (Hibernate) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Email | SendGrid Java SDK |
| Caching | Spring Cache |
| Build Tool | Maven (Maven Wrapper included) |
| Containerization | Docker (multi-stage build) |
| Code Generation | Lombok |

---

## ✨ Features

- **JWT-based Authentication** — Secure login and token-based session management
- **Expense Tracking** — Create, read, update, and delete expense records
- **Budget Management** — Enterprise-grade budget and expense oversight
- **Role-based Access Control** — Spring Security integration for user roles
- **Database Migrations** — Automated schema versioning with Flyway
- **Email Notifications** — Transactional emails via SendGrid
- **Response Caching** — Performance optimization using Spring Cache
- **API Documentation** — Interactive Swagger UI via SpringDoc OpenAPI
- **Dockerized Deployment** — Multi-stage Docker build for lean production images
- **Input Validation** — Bean Validation (Jakarta) on all request DTOs

---

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 21+**
- **Maven 3.9+** (or use the included `./mvnw` wrapper)
- **MySQL** (running locally or via Docker)
- **Docker** (optional, for containerized deployment)
- A **SendGrid API key** (for email functionality)

---

## ⚙️ Configuration

Create or update `src/main/resources/application.properties` (or `application.yml`) with your environment values:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/expense_manager
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# JWT
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration-ms=86400000

# SendGrid
sendgrid.api-key=your_sendgrid_api_key
sendgrid.from-email=no-reply@yourdomain.com
```

> **Note:** Never commit secrets to version control. Use environment variables or a secrets manager in production.

---

## 🏃 Running Locally

### 1. Clone the Repository

```bash
git clone https://github.com/rajendra504/expense-manager-backend.git
cd expense-manager-backend
```

### 2. Build & Run with Maven

```bash
./mvnw spring-boot:run
```

Or build the JAR and run it:

```bash
./mvnw clean package -DskipTests
java -jar target/expense-manager-0.0.1-SNAPSHOT.jar
```

The API will start at **http://localhost:8080**

---

## 🐳 Running with Docker

### Build the Docker Image

```bash
docker build -t expense-manager-backend .
```

### Run the Container

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/expense_manager \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e APP_JWT_SECRET=your_jwt_secret \
  -e SENDGRID_API_KEY=your_sendgrid_key \
  --name expense-manager \
  expense-manager-backend
```

### Docker Compose (Recommended)

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: expense_manager
      MYSQL_ROOT_PASSWORD: secret
    ports:
      - "3306:3306"

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/expense_manager
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: secret
    depends_on:
      - db
```

```bash
docker-compose up --build
```

---

## 📖 API Documentation

Once the application is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI JSON spec is available at:

```
http://localhost:8080/v3/api-docs
```

---

## 🔐 Authentication Flow

1. **Register** — `POST /api/auth/register` with username, email, and password
2. **Login** — `POST /api/auth/login` returns a JWT access token
3. **Use the token** — Pass the token in the `Authorization` header as `Bearer <token>` for all protected endpoints

---

## 🗄️ Database Migrations

This project uses **Flyway** to manage database schema changes. Migration scripts are located in:

```
src/main/resources/db/migration/
```

Migrations run automatically on application startup. Scripts follow the naming convention `V{version}__{description}.sql`.

---

## 🏗️ Project Structure

```
expense-manager-backend/
├── src/
│   ├── main/
│   │   ├── java/com/rajendra/expensemanager/
│   │   │   ├── config/          # Security, JWT, and app configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Request/Response DTOs
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Global exception handling
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   └── service/         # Business logic layer
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/    # Flyway SQL migration files
│   └── test/                    # Unit and integration tests
├── .mvn/wrapper/                # Maven wrapper config
├── Dockerfile                   # Multi-stage Docker build
├── pom.xml                      # Maven dependencies
└── mvnw / mvnw.cmd              # Maven wrapper scripts
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 👤 Author

**Rajendra** — [@rajendra504](https://github.com/rajendra504)

---

## 📄 License

This project is open source. See the repository for license details.
