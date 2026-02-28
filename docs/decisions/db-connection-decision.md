Database Connection Decision Record
ADR-002: Choosing the Best Database Connection Method
1. Decision Summary

This system needs a reliable database connection because it must store and retrieve important data such as:

Users (for login/authentication)

Guest details

Reservations

Room types and pricing

Since the application is built as a Java EE system running on Tomcat, and the database is MySQL hosted using WAMP, the database connection method must be fast, stable, and able to support multiple users at the same time without issues.

2. Options Considered
Option 1: Direct JDBC Connection using DriverManager (new connection every time)

How it works:
For each database request, the program:

opens a connection

runs the query

closes the connection

✅ Advantages

Very easy to implement

No server-side configuration is needed

Simple to understand, especially for beginners

❌ Disadvantages

Slow when used repeatedly because opening connections takes time

Not suitable if many users are using the system

Higher chance of errors if connections are not closed properly

Option 2: Singleton Shared Connection (one global connection for the whole system)

How it works:
The application creates one database connection and uses it everywhere.

✅ Advantages

Very little setup needed

Avoids opening a new connection for every request

❌ Disadvantages

Not safe when multiple requests happen at the same time (thread safety issues)

If the connection fails, the whole system stops working

Not considered a professional solution for enterprise applications

Option 3: Connection Pooling using Tomcat JNDI DataSource (Chosen)

How it works:
Tomcat manages a pool of ready-to-use database connections.
Instead of creating connections manually, the application requests one from a DataSource, uses it, and returns it back to the pool.

✅ Advantages

Faster and more efficient because connections are reused

Works much better in multi-user situations

Reduces resource waste and improves system stability

This is a standard approach in real Java EE applications

Database settings can be managed in Tomcat configuration instead of hardcoding them inside the code

❌ Disadvantages

Requires extra configuration in Tomcat

Slightly more advanced than the DriverManager method

3. Final Decision

✅ The system will use Option 3: Tomcat JNDI DataSource with connection pooling.

4. Justification (Why This Choice Makes Sense)

This decision supports good software engineering practices because:

Better performance and scalability: connection pooling avoids creating new connections repeatedly.

More reliable system behavior: Tomcat manages the pool and connections more safely than doing everything manually.

Cleaner design: database connection details stay in Tomcat configuration instead of being hardcoded inside the application.

Easier maintenance: if database details change (host, username, password), they can be updated in Tomcat settings without changing the Java code.

Overall, this approach matches the goal of building a more professional and enterprise-like Java EE system.

5. Risks and How We Will Handle Them

Risk: Tomcat configuration errors may stop the application from connecting to the database.
✅ Solution: configuration will be documented clearly and tested early.

Risk: If connections are not returned properly, the pool could run out of available connections.
✅ Solution: ensure every connection is closed correctly (using finally blocks or proper structured handling), so it returns to the pool safely.

6. Evidence to Include (Screenshots)

The final report will include proof such as:

Screenshot showing WAMP MySQL service running

Screenshot of database structure in phpMyAdmin

Screenshot of Tomcat JNDI settings (context.xml or server configuration)

Console/log output showing a successful database connection test

Application logs proving connections are obtained correctly from the pool