# Testing Strategy & Test Plan (Task C)

## 1. Testing objectives
The goal of testing is to verify that the reservation system:
- meets functional requirements (login, reservation CRUD, bill, help, exit),
- prevents invalid input and invalid states (validation),
- behaves correctly under typical and boundary conditions,
- remains stable when interacting with the database,
- is suitable for a distributed Java EE deployment on Tomcat with a MySQL database (WAMP).

This plan is aligned with the requirement to document the test plan, apply test-driven development, and provide evidence of tests used to verify the system.

---

## 2. Selected test strategies (3+), with justification

### Strategy 1 — Unit Testing (White-box) with JUnit
**What is tested:**
Business logic that can be validated without the full server environment, for example:
- date validation (check-out after check-in),
- number-of-nights calculation,
- bill calculation (rate × nights),
- reservation number format rules,
- input validation methods.

**Why this strategy is suitable for my system:**
- The business rules live in service classes; unit tests verify correctness early.
- Unit tests are fast, repeatable, and support refactoring safely.
- This fits a layered architecture because the service layer can be tested independently.

**Evidence:**
- Screenshots of JUnit tests passing (green bar) and console test report output.

---

### Strategy 2 — Integration Testing (Service ↔ DAO ↔ Database)
**What is tested:**
Database interaction correctness:
- insert reservation, retrieve reservation, update, delete,
- constraints/uniqueness behavior,
- transaction/rollback behavior (if implemented),
- handling database failures (e.g., invalid credentials, DB down).

**Why this strategy is suitable for my system:**
- The system is database-driven; correctness depends on persistence being reliable.
- Many defects happen at boundaries (SQL queries, mapping fields, connection handling).
- Since deployment is on Tomcat + WAMP, integration testing validates real environment behavior.

**Evidence:**
- Screenshots of database records in phpMyAdmin after test execution.
- Screenshots/log outputs showing successful DAO operations.

---

### Strategy 3 — System / End-to-End Testing (Black-box)
**What is tested:**
Complete user scenarios through the running application:
- login → add reservation → view reservation → generate bill,
- invalid input handling and user-friendly messages,
- navigation to Help and Exit flow,
- distributed execution behavior on Tomcat (client request → server logic → DB).

**Why this strategy is suitable for my system:**
- Confirms that components work together as a distributed application.
- Validates that user requirements are met from the user’s perspective.
- Ensures UI-level validation and server-level validation are consistent.

**Evidence:**
- Screenshots of UI pages / console outputs for each scenario.
- Screenshot of Tomcat running the application successfully.

---

### (Optional additional strategy to strengthen marks) Strategy 4 — Negative & Boundary Value Testing
**What is tested:**
Invalid inputs and boundary conditions:
- empty fields, invalid phone length, invalid dates, extremely long names/addresses,
- SQL injection-style strings (to confirm safe handling),
- boundary dates (same-day booking, check-out equals check-in should fail).

**Why it adds value:**
- Reservation systems must prevent invalid states to avoid booking conflicts and billing errors.
- Demonstrates defensive programming and validation rigor.

---

## 3. Test-Driven Development (TDD) approach (how and why)
I applied TDD primarily at the **service layer**:
1. Write a failing test for a rule (e.g., check-out must be after check-in).
2. Implement the minimal code to pass the test.
3. Refactor to improve design (keeping tests green).

TDD is used because:
- it reduces defect leakage,
- it forces clear method contracts,
- it provides regression protection during iterative UP development.

---

## 4. Test environment
- Application server: Apache Tomcat
- Database: MySQL (WAMP)
- Test framework: JUnit (allowed in test environment)
- Evidence collection: screenshots of tests, logs, and database state

---

## 5. Test data design
Test data is derived to cover:
- normal cases (valid reservations),
- boundary cases (min/max lengths, date edges),
- negative cases (invalid formats, missing required fields),
- realistic business data (different room types and rates).

A consistent naming convention is used:
- Users: admin/receptionist accounts
- Reservation numbers: R0001, R0002… (or system-generated)
- Room types: SINGLE, DOUBLE, FAMILY, SUITE with defined rates
