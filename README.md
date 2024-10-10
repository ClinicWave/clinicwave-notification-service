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
- [Mailtrap](https://mailtrap.io) account for email testing

### Steps

Before proceeding, please read the [contributing guidelines](CONTRIBUTING.md).

1. Clone the repository:
   ```sh
   git clone https://github.com/ClinicWave/clinicwave-notification-service.git
   cd clinicwave-notification-service
    ```

2. Run docker-compose to start the required services:
    ```sh
    docker compose up -d
    ```

3. Update the existing `secrets.properties` file in the `src/main/resources` directory with your Mailtrap credentials:
   ```sh
   MAILTRAP_USERNAME=<your-mailtrap-username>
   MAILTRAP_PASSWORD=<your-mailtrap-password>
   ```
   **Note:** You need to sign up for a [Mailtrap](https://mailtrap.io) account to get the `MAILTRAP_USERNAME` and
   `MAILTRAP_PASSWORD`. After signing up:
    - Create a new inbox.
    - Go to the "Integration" tab.
    - Choose "SMTP" integration.
    - Copy the `username` and paste it into `MAILTRAP_USERNAME` in `secrets.properties`.
    - Copy the `password` and paste it into `MAILTRAP_PASSWORD` in `secrets.properties`.

4. Build the project:
    ```sh
    ./mvnw clean install
    ```
5. Run the application:
    ```sh
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=docker
    ```

6. Alternatively, you can use the provided `start-backend.sh` script to start the backend

   - Ensure the `start-backend.sh` script has execute permissions. If not, grant execute permissions:
       ```sh
       cd installer
       chmod +x start-backend.sh
       ```

   - Run the script to start the backend:
       ```sh
       ./installer/start-backend.sh
       ```

## Stopping the service

To stop the backend service, you have multiple options:

1. Stop Through the User Interface:

    - If your application has a user interface with a stop button, you can simply click the stop button to gracefully
      terminate the backend service.

2. Using Docker Compose:

    - Navigate to the service directory and run the following command to stop the services started by Docker Compose:

        ```sh
        cd clinicwave-notification-service
        docker compose down
        ```

    - This command stops and removes the containers created by `docker compose up -d`.

3. Using the Provided Script:

    - You can stop the backend using the provided `stop-backend.sh` script.

    - Ensure the `stop-backend.sh` script has execute permissions. If not, grant execute permissions:
        ```sh
        cd installer
        chmod +x stop-backend.sh
        ```

    - Run the script to stop the backend:

       ```sh
       cd installer
       ./stop-backend.sh
        ```

    - This script will terminate the Spring Boot application and stop any related services as defined in the script.

### API Endpoints

#### Notifications

- Send notification: `POST /api/notifications/send`

### Contributing

We welcome contributions to this project! If you'd like to contribute, please read
our [contributing guidelines](CONTRIBUTING.md) first.

### Contact

For any questions or feedback, please feel free to reach out to the repository owner
at [aamirshaikh3232@gmail.com](aamirshaikh3232@gmail.com).

