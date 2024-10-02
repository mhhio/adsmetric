# Ad Metrics API

## Overview
This project is a Spring Boot 3 application that provides an API for ad metrics. It uses Java 17 and PostgreSQL for data storage. The API is documented using Swagger.

## Features
- RESTful API for ad metrics
- PostgreSQL database integration
- Swagger API documentation
- Unit tests for calculation and analytical process
- Dockerized application and database setup

## Prerequisites
- Java 17
- Docker and Docker Compose
- Maven

## Getting Started

### Clone the repository
```bash
git clone https://github.com/mhhio/adsmetric.git
cd adsmetric
```

### Build the application
```bash
./mvnw clean package
```


### Run with Docker Compose
```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

## API Documentation
Swagger UI is available at `http://localhost:8080/swagger-ui.html`

## Database
PostgreSQL database is running on port 5432. You can connect to it using the following credentials:
- Database: adsmetric
- Username: root
- Password: postgres

pgAdmin is available at `http://localhost:5050` for database management.
- Email: admin@example.com
- Password: 123456A!

## Project Structure
```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── adsmetric/
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── repository/
│   │   │               ├── model/
│   │   │               └── AdMetricApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── adsmetric/
│                       └── ...
├── data
├── request.curl
├── Dockerfile
├── docker-compose.yml
├── pom.xml (or build.gradle)
└── README.md
```

## Configuration
Application configuration can be found in `src/main/resources/application.properties`.

## Testing
To run tests:
```bash
./mvnw test
```
