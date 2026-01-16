Unified Process (UP) Plan – Ocean View Resort Room Reservation System

1\. Why we are using the Unified Process (UP)



This project follows the Unified Process (UP) because it supports building software in small steps (iterations) instead of trying to finish everything at once. UP is also risk-focused, meaning we deal with the most important and risky parts of the system early—such as architecture, database connectivity, and service communication.



This approach fits our assignment well because the Ocean View Resort system is not a simple program. It needs to be a distributed Java EE application with web services, database integration, and proper software design practices, so planning and validating the design early helps avoid problems later.



2\. UP Phases and What We Will Produce

2.1 Inception Phase (Starting the Project)



Goal: Understand what the system should do and define the project boundaries clearly.



Main outputs:



A clear system scope for the Ocean View Resort Room Reservation System



The main actors (such as Receptionist and Admin)



A list of key use cases (Login, Add Reservation, View Reservation, Generate Bill, Help, Exit)



Important assumptions (room categories, pricing method, reservation ID uniqueness)



An early risk list (database errors, double bookings, incorrect user inputs)



Why this phase is important:

It helps us avoid confusion later by making sure everyone understands what we are building from the beginning.



2.2 Elaboration Phase (Design and Architecture)



Goal: Build a strong design plan and confirm the architecture before writing a lot of code.



Main outputs:



A finalized distributed architecture plan (Java EE app + Tomcat + database server)



Database design (tables and how they are related)



UML diagrams:



Use Case Diagram (actors + interactions)



Class Diagram (attributes, methods, relationships, access modifiers)



Sequence Diagrams (at least 3 main workflows)



Pattern selection with justification:



Architecture pattern (3-tier architecture)



Design patterns (DAO, Factory, Singleton if needed)



Validation plan (rules for checking input at UI/service level)



Why this phase is important:

This stage prevents bad design decisions. It ensures the system is easier to maintain, well-structured, and follows good software practices like modularity, encapsulation, and separation of concerns.



2.3 Construction Phase (Coding and Testing)



Goal: Build the system step by step while continuously testing each part.



Main outputs:



Development broken into clear modules:



Login/Authentication



Reservation management



Billing



Reports



Java EE services connected to the database



Implementation of chosen design patterns (visible in the code structure)



Unit testing using JUnit



Integration testing (checking service + database workflow together)



Evidence such as screenshots of outputs and testing



Why this phase is important:

Since the system is built in small stages, errors can be found early and fixed faster instead of becoming bigger problems later.



2.4 Transition Phase (Final Deployment and Validation)



Goal: Deploy the final system, confirm everything works, and prepare the final submission.



Main outputs:



Deployment on Apache Tomcat



Database setup and proof (WAMP + MySQL)



Final testing proof (50 test cases + screenshots where required)



User guide / Help documentation



Final reflection and improvements section



Public GitHub repo link with full commit history



A final release version/tag (example: v1.0 Final Submission)



Why this phase is important:

This is where everything is finalized and confirmed as working, including documentation and evidence needed for assessment.



3\. Iteration Plan (Step-by-Step Development)

Iteration 1 – Authentication + Database Connection



Focus:



Login validation



Choosing the database connection approach and explaining why



Testing the connection and collecting proof



Outcome:



A working and secure login entry point



A stable, tested database connection



Iteration 2 – Reservation Management (CRUD)



Focus:



Add reservation



View reservation details



Input validation (dates, phone number, required fields)



Outcome:



Core reservation features working properly with database storage



Iteration 3 – Billing + Reports



Focus:



Calculate number of nights



Apply room pricing rules



Generate a bill summary/output



Include useful reports (reservation list, occupancy summary)



Outcome:



Correct billing and extra reporting features added



Iteration 4 – Full Testing + Deployment Documentation



Focus:



Completing 50 test cases



Using multiple testing methods (unit, integration, system/usability)



Deployment on Tomcat with screenshots



Final report completion + good Git usage under UP workflow



Outcome:



A fully tested and deployed system with strong documentation and evidence



4\. Evidence We Will Collect (Screenshots / Proof)



The following evidence will be gathered and added to the final documentation:



Screenshot of this UP-plan.md file



GitHub commit history showing the system being built step by step



Screenshots of system pages (login, reservation form, billing output)



Database screenshots (tables + sample records from WAMP/phpMyAdmin)



JUnit test execution screenshots



Deployment proof on Apache Tomcat



Screenshots of key results (reservation display, bill generation, reports)



5\. Final Summary



Overall, the Unified Process helps us build this project in a structured and professional way. By confirming the design early during the Elaboration phase, we reduce technical risks and avoid making poor architectural decisions. By developing in iterations, we can test and improve each feature gradually. This ensures the final system is well-planned, maintainable, and supported with clear documentation and evidence for submission.

