Architecture Decision Record (ADR)
ADR-001: Distributed Architecture for Ocean View Resort Reservation System
1. Decision Summary

For this project, the Ocean View Resort Reservation System will be built as a distributed Java EE application and deployed on Apache Tomcat. The main business features will be provided through web services, and the client side will communicate with the server remotely. The database will run on MySQL using WAMP.

This architecture allows the system to support key functions such as:

User Login (Authentication)

Add a New Reservation

View Reservation Details

Calculate and Print the Bill

Help Section

Exit the System

2. Main Requirements Behind This Decision

The system needs to:

Work as a distributed application using web services

Store data in a proper database

Use suitable architectural and design patterns

Provide a simple and user-friendly interface (even a basic one is fine)

Be easy to maintain and include proper validation

3. Options We Considered
Option A: 3-Tier Java EE Architecture with Web Services (Chosen)

Structure

Presentation Layer: JSP/HTML web UI OR a simple console client

Business Layer: Service classes (business rules + validation)

Data Access Layer: DAO classes using JDBC

Server: Apache Tomcat

Database: MySQL (WAMP)

✅ Advantages

Clearly shows a proper distributed system

Separates UI, business rules, and database logic (clean structure)

Easier to maintain, test, and upgrade

Fits real-world systems (Tomcat + Database setup)

Allows different clients later (web UI, console app, etc.)

❌ Disadvantages

Needs slightly more setup than a simple Java program

Requires good documentation and careful organization

Option B: Single-Tier Application (UI + logic + database in one layer)

✅ Advantages

Faster to build

❌ Disadvantages

Hard to maintain and improve

Difficult to test properly

Does not clearly show a distributed architecture

UI becomes tightly connected to business logic

Option C: SOAP Web Services (JAX-WS)

✅ Advantages

Strong structure and strict messaging format

❌ Disadvantages

More complex than necessary for this assignment

Uses heavy XML communication

Takes more time to set up compared to REST-style services

4. Final Decision

✅ We selected Option A:
A 3-tier Java EE architecture deployed on Tomcat, using web services and a MySQL database.

5. Architectural Pattern Justification (Why This Works)
5.1 Three-Tier Architecture

This system follows the Three-Tier Architecture model:

1) Presentation Tier

Handles user input and displays system output

Keeps UI logic separate from system rules

2) Business Tier

Contains validation and important system rules such as:

check-in / check-out date validation

bill calculation

checking availability (if implemented)

Makes the system reusable and easy to test

3) Data Access Tier

Uses DAO classes to communicate with the database

Prevents SQL logic from being mixed with business code

This architecture improves:

Maintainability (UI changes won’t break database code)

Scalability (new clients can be added later)

Testability (business layer can be tested using JUnit)

Separation of concerns (clean, professional structure)

6. Design Principles Used
Encapsulation

All class attributes will be private, and access will be controlled using getters and setters.
This protects internal data and avoids unwanted changes.

Single Responsibility Principle (SRP)

Each class will have only one main job, for example:

ReservationService → reservation rules + validation

ReservationDAO → database operations

ReservationController / Servlet → handles requests and responses only

Low Coupling & High Cohesion

The system keeps components independent from each other (low coupling), and keeps related logic together (high cohesion). This makes the project cleaner and easier to manage.

7. High-Level System Components

The system will include:

User Interface module (web or console)

Authentication module

Reservation management module

Billing module

Reporting module (optional value-added feature)

Database module (JDBC)

8. Evidence to Include (Screenshots)

These screenshots will be used as proof in the final report:

Successful deployment on Tomcat (server running + app URL)

UI forms or console output screenshots

Web service endpoint testing (browser/Postman if allowed)

Database table structure + sample data (phpMyAdmin)

Screenshot of project folder/package structure

Billing output and reservation display proof