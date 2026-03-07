# Ocean-View-Resort-Room-Reservation

Online Room Reservation System for Advanced Programming Assignment - CIS6003



\# Ocean View Resort Room Reservation System



Java EE web application developed for the CIS6003 Advanced Programming assignment. The system supports staff-side reservation handling, billing, payments, guest check-in/check-out, room and room-type management, user administration, reporting, and email notifications.



\## Main features

\- User authentication with role-based access

\- Reservation creation, search, view, confirm, cancel, check-in, and check-out

\- Automatic bill generation and payment recording

\- Room, room type, and user CRUD operations for admin users

\- Dashboard/reporting views

\- Gmail-based reservation notifications

\- MySQL stored procedures, triggers, events, and relational constraints



\## Project structure

\- `src/server/OceanViewServer/` - main Java EE web application

\- `database/` - MySQL schema and seed/export script

\- `docs/` - UML, testing, screenshots, and development notes

\- `LICENSE` - repository licence information



\## Technologies used

\- Java 11

\- JSP / Servlets

\- Maven

\- Apache Tomcat 9

\- MySQL

\- JUnit



\## Quick start

1\. Import the SQL file from `database/ocean\_view\_resort.sql` into MySQL.

2\. Configure the JDBC resource used by Tomcat.

3\. Open `src/server/OceanViewServer/` as a Maven project in IntelliJ IDEA.

4\. Build and deploy the application to Tomcat.

5\. Access the application through the configured local server URL.



\## Notes

\- This repository was organised to support both implementation and academic documentation.

\- Detailed database notes are in `database/README.md`.

\- Application-side setup notes are in `src/server/OceanViewServer/README.md`.



