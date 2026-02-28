# Design Decision Log (DDL)
## Ocean View Resort – Online Room Reservation System (Java EE / Tomcat / WAMP)

## Purpose
This Design Decision Log records the key software engineering decisions made during development.
For each decision, multiple options were considered, pros/cons were evaluated, and the final choice
was justified using theoretical software engineering principles.

This demonstrates the ability to make correct engineering decisions when designing a distributed system.

---

# DDL-001 — Application Architecture Pattern
**Decision:** Select the architecture style for the distributed system.

**Options considered:**
1. Single-layer application (UI + business + DB in one)
2. 2-tier (UI + DB)
3. 3-tier architecture (Presentation + Business + Data Access) ✅ Chosen

**Pros/Cons:**
- Single-layer:
  - ✅ Quick implementation
  - ❌ Hard to maintain, test, and extend (tight coupling)
- 2-tier:
  - ✅ Simpler than 3-tier
  - ❌ Business logic spreads across layers and becomes difficult to test
- 3-tier:
  - ✅ Separation of concerns, scalability, maintainability
  - ✅ Supports professional distributed enterprise design
  - ❌ More initial structure and documentation required

**Chosen option:** 3-tier architecture

**Justification (theory-aligned):**
This choice supports separation of concerns and improves maintainability and testability. It also enables
clean layering in Java EE, where UI and business rules remain independent from database logic.

**Evidence to capture:**
Architecture diagram + package structure screenshot.

---

# DDL-002 — Communication Style (Distributed Web Services)
**Decision:** Choose the communication mechanism between client and server.

**Options considered:**
1. Direct DB access from UI (not distributed)
2. SOAP Web Services
3. REST-style services using Java EE APIs ✅ Chosen

**Pros/Cons:**
- Direct DB access:
  - ✅ Simple
  - ❌ Not distributed, insecure, breaks tier separation
- SOAP:
  - ✅ Formal and strict contracts
  - ❌ Heavy and complex for this system
- REST-style services:
  - ✅ Lightweight, supports multiple clients easily
  - ✅ Stateless communication improves scalability
  - ❌ Requires careful endpoint design and validation

**Chosen option:** REST-style services

**Justification:**
REST services align with distributed system principles and allow multiple clients (simple web UI and/or
console-based client) to reuse the same business logic through controlled interfaces.

**Evidence:**
Endpoint screenshots + service flow diagram.

---

# DDL-003 — Database Type and Storage Method
**Decision:** Select storage mechanism for reservation data.

**Options considered:**
1. Text file storage
2. Relational database (MySQL via WAMP) ✅ Chosen

**Pros/Cons:**
- Text files:
  - ✅ Easy to implement
  - ❌ Poor integrity, difficult searching, no concurrency control
- Relational database:
  - ✅ Data integrity via constraints
  - ✅ Supports queries and reporting
  - ✅ Realistic and scalable
  - ❌ Requires schema design and connection configuration

**Chosen option:** MySQL relational database

**Justification:**
A relational database provides data consistency, structured storage, and supports professional reservation management.
It also aligns with the requirement to use a “proper database”.

**Evidence:**
ERD + phpMyAdmin screenshots of tables and records.

---

# DDL-004 — Database Connection Strategy
**Decision:** Select the optimal DB connection approach.

**Options considered:**
1. DriverManager per operation
2. Singleton global connection
3. Tomcat JNDI DataSource connection pooling ✅ Chosen

**Chosen option:** Tomcat pooling (JNDI DataSource)

**Justification:**
Connection pooling is industry standard for Java EE apps hosted on Tomcat, improving performance and reliability.
It also centralises configuration and supports maintainability.

**Evidence:**
Tomcat config + connection test logs + DAO operations proof.

---

# DDL-005 — Layering and Responsibility Allocation (SRP)
**Decision:** Decide where to place business validation rules.

**Options considered:**
1. Validation only in UI layer
2. Validation only in DAO layer
3. Validation in service layer + UI boundary checks ✅ Chosen

**Pros/Cons:**
- UI only:
  - ✅ User-friendly immediate feedback
  - ❌ Unsafe (bypassed if another client calls services)
- DAO only:
  - ✅ Stops invalid DB writes
  - ❌ Too late; mixes persistence with business rules
- Service + UI:
  - ✅ Defensive programming
  - ✅ Consistent rules across clients
  - ✅ Supports SRP and clean architecture

**Chosen option:** Service layer validation + UI validation for usability

**Justification:**
Placing key validation in the service layer ensures correctness and prevents invalid states even if the UI is bypassed.
UI validation improves user experience through early feedback.

**Evidence:**
Validation code screenshot + negative test evidence.

---

# DDL-006 — Encapsulation and Access Modifiers
**Decision:** Choose visibility for class attributes and methods.

**Options considered:**
1. public fields
2. private fields + getters/setters ✅ Chosen

**Chosen option:** private fields + getters/setters

**Justification:**
Private access modifiers enforce encapsulation by protecting internal state.
Getters and setters provide controlled access and support future validation rules.

**Evidence:**
Class diagram showing access modifiers + code screenshot.

---

# DDL-007 — Constructors Design (Default vs Parameterized)
**Decision:** How objects should be created and initialized.

**Options considered:**
1. Only default constructors + setters
2. Only parameterized constructors
3. Both default + parameterized constructors ✅ Chosen

**Chosen option:** Both

**Justification:**
Default constructors support tool/framework compatibility and flexible object creation.
Parameterized constructors ensure required attributes are provided at creation, improving correctness and object integrity.

**Evidence:**
Code screenshot + class diagram showing constructors.

---

# DDL-008 — Collection Type for In-Memory Operations
**Decision:** Select optimal collections for temporary storage and lookups.

**Options considered:**
1. ArrayList
2. LinkedList
3. HashMap ✅ (for lookup)
4. TreeMap

**Chosen option:** ArrayList + HashMap (depending on operation)

**Justification:**
ArrayList supports ordered iteration and is efficient for sequential processing.
HashMap provides average O(1) lookup using reservation number as the key, which is suitable for search operations.

**Evidence:**
Service logic screenshot + reasoning section in report.

---

# DDL-009 — Reservation Number Generation Approach
**Decision:** Decide how reservation numbers are created.

**Options considered:**
1. User manually enters reservation number
2. Auto-generated unique reservation number ✅ Chosen

**Chosen option:** auto-generated

**Justification:**
Auto-generation reduces human error and ensures uniqueness. It prevents collisions and improves reliability in reservation systems.

**Evidence:**
Code/logic screenshot + test case showing uniqueness.

---

# DDL-010 — Billing Calculation Logic Placement
**Decision:** Choose where billing rules should be implemented.

**Options considered:**
1. UI layer
2. DAO layer
3. BillingService class ✅ Chosen

**Chosen option:** BillingService

**Justification:**
Billing rules are business logic and should remain independent from UI and persistence.
This supports SRP, testability, and reuse across different clients.

**Evidence:**
Billing service screenshot + JUnit tests for calculation.

---

# DDL-011 — Error Handling Strategy
**Decision:** Decide how errors will be handled and reported.

**Options considered:**
1. Show raw exception messages to users
2. Show user-friendly messages + log technical details ✅ Chosen

**Chosen option:** user-friendly messages + logs

**Justification:**
This improves usability and security. Raw exceptions may expose sensitive system details.
Logging supports debugging and maintenance without harming user experience.

**Evidence:**
Screenshots of error messages + log output evidence.

---

# DDL-012 — Authentication and Password Storage
**Decision:** Decide how passwords are stored.

**Options considered:**
1. Plain text passwords (unsafe)
2. Hashed passwords ✅ Chosen

**Chosen option:** hashed passwords

**Justification:**
Hashing improves security and supports ethical and professional software practice.
Even in an academic system, protecting user data is important.

**Evidence:**
DB screenshot showing hashed password values + explanation.

---

# DDL-013 — Testing Strategy Selection
**Decision:** Select testing approaches for verification.

**Options considered:**
1. Only manual testing
2. Only unit tests
3. Mixed strategy: Unit + Integration + System testing ✅ Chosen

**Chosen option:** mixed strategy

**Justification:**
Unit tests verify logic, integration tests validate DB and boundary interactions,
and system tests confirm real user flows. This improves coverage and reduces defect risk.

**Evidence:**
JUnit screenshot + system test screenshots + DB evidence.

---

# DDL-014 — Version Control under Unified Process
**Decision:** Decide how version control aligns with UP phases.

**Options considered:**
1. One final commit at the end
2. Continuous commits aligned to iterations ✅ Chosen

**Chosen option:** commits aligned to UP iterations

**Justification:**
UP promotes incremental delivery. Frequent commits provide traceability,
allow rollback, and demonstrate professional development workflow.

**Evidence:**
GitHub commit history screenshot + tagged versions (v0.1, v0.2, v1.0).

---

# Summary
The decisions in this log demonstrate that the system was designed using professional software engineering principles,
considering multiple alternatives and selecting the most suitable solutions based on maintainability, scalability,
reliability, usability, and distributed system requirements.
