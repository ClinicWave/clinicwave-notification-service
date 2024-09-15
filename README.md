# Clinicwave Notification Service

## Project Overview

The Clinicwave Notification Service is a Spring Boot application designed to handle notifications for the Clinicwave.
This service is responsible for sending notifications to users based on various events and triggers within the system.
It provides a centralized way to manage and deliver notifications to users, ensuring timely and relevant communication.
The service integrates with other parts of the Clinicwave system to provide a seamless user experience.

## Features

- Send email notifications
- Send SMS notifications (TBD)
- Send push notifications (TBD)
- Handle notification templates
- Integrate with user management service for user-specific notifications
- RESTful API endpoints
- Kafka integration for event-driven notifications

## Architecture

The service is structured as a Spring Boot application with the following major components:

- **Controllers**: Handle HTTP requests and map them to service methods.
- **Services**: Encapsulate business logic and provide a layer of abstraction between controllers and repositories.
- **Strategy**: Contains strategies for sending notifications via different channels (e.g., email, SMS, push
  notifications).
- **Repositories**: Interface with the database using JPA/Hibernate for data persistence.
- **Configuration**: Includes setup for SmtpSetting (for email notifications), Kafka consumer configuration.
- **Domain**: Contains domain models representing core entities in the system.
- **DTO**: Defines Data Transfer Objects used for API communication.
- **Enums**: Enumerations for defining various constants used throughout the application.
- **Exception**: Custom exceptions and error handling logic.

The service integrates with other parts of the ClinicWave system through RESTful APIs and event-driven messaging using
Kafka.

## Installation and Setup

### Prerequisites

- Docker
- Java 17 or higher
- Maven 3.6.0 or higher

### Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/ClinicWave/clinicwave-notification-service.git
   cd clinicwave-notification-service
    ```

2. Run docker-compose to start the required services:
    ```sh
    docker-compose up -d
    ```

3. Create `.env` in the project root and add the following environment variables:
   ```sh
   DOCKER_POSTGRES_USERNAME=<your-docker-postgres-username>
   DOCKER_POSTGRES_PASSWORD=<your-docker-postgres-password>
   ```
4. Create `secrets.properties` in the `src/main/resources` directory and add the following properties:
   ```sh
   MAILTRAP_USERNAME=<your-mailtrap-username>
   MAILTRAP_PASSWORD=<your-mailtrap-password>
   DOCKER_POSTGRES_USERNAME=<your-docker-postgres-username>
   DOCKER_POSTGRES_PASSWORD=<your-docker-postgres-password>
   ```
   **Note:** You need to sign up for a [Mailtrap](https://mailtrap.io) account to get the `MAILTRAP_USERNAME` and `MAILTRAP_PASSWORD`. After signing up:
   - Create a new inbox.
   - Go to the "Integration" tab.
   - Choose "SMTP" integration.
   - Copy the `username` and paste it into `MAILTRAP_USERNAME` in `secrets.properties`.
   - Copy the `password` and paste it into `MAILTRAP_PASSWORD` in `secrets.properties`.

5. Build the project:
    ```sh
    mvn clean install
    ```
6. Run the application:
    ```sh
    mvn spring-boot:run
    ```
7. Testing the application:
    ```sh
    mvn test
    ```

### API Endpoints

#### Notifications

- Send notification: `POST /api/notifications/send`

### Contributing

1. Fork the repository
2. Create a new branch (`git checkout -b feature`)
3. Make changes and commit them (`git commit -am 'Add new feature'`)
4. Push the changes to the branch (`git push origin feature`)
5. Create a pull request
6. Get your changes reviewed and merged
7. Happy coding!

### Contact

For any questions or feedback, please feel free to reach out to the repository owner
at [aamirshaikh3232@gmail.com](aamirshaikh3232@gmail.com).

