\# Database README



This folder contains the MySQL database export for the Ocean View Resort Reservation System.



\## File

\- `ocean\_view\_resort.sql` - complete schema/data export used for local development and testing



\## Database contents

The SQL script includes:

\- core tables such as users, guests, reservations, rooms, room\_types, bills and payments

\- primary keys, foreign keys and integrity constraints

\- stored procedures used for application logic such as billing/payment-related operations

\- triggers and scheduled events where applicable

\- seed/sample data required to run the system locally



\## How to use

1\. Create a database named `ocean\_view\_resort` in MySQL.

2\. Import `ocean\_view\_resort.sql` using phpMyAdmin, MySQL Workbench, or the MySQL CLI.

3\. Make sure the application JDBC configuration points to the same schema.



\## Important note

If the local MySQL server uses a different username, password, or port, update the Tomcat/JDBC configuration accordingly before running the web application.



