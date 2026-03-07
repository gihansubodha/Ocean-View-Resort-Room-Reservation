# OceanViewServer README

This is the main Java EE web application for the Ocean View Resort Room Reservation System.

## Main responsibilities
- Handle HTTP requests through JSP pages and servlets
- Apply business logic through service classes
- Access MySQL through DAO interfaces and implementations
- Generate bills, update reservation status, and manage room allocation
- Send reservation-related email notifications

## Build and run
1. Open this folder as a Maven project.
2. Resolve dependencies from `pom.xml`.
3. Configure Apache Tomcat 9 in IntelliJ IDEA.
4. Configure the required JDBC resource and Gmail environment variables if notification features are enabled.
5. Build and deploy the project.

## Key layers
- `web` - servlets/controllers
- `service` - business rules
- `dao` - persistence layer
- `model` - entity and DTO classes
- `notify` - notification, event, and template classes
- `util` - shared utility classes

## Deployment artifact
The application is intended to be packaged and deployed as a WAR to Tomcat.
