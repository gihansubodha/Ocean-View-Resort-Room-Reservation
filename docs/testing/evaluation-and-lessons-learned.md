# Evaluation and Lessons Learned (Critical Reflection)

## 1. Evaluation approach
To evaluate the overall success of the solution, I reviewed the system from multiple perspectives:
- Functional completeness against the assessment scenario requirements.
- Architectural quality (layering, modularity, coupling/cohesion).
- Data integrity and reliability of persistence.
- Validation robustness and error handling quality.
- Test coverage and evidence quality (automation + screenshots).
- Maintainability and ability to extend the system.

This evaluation is supported by traceability (requirements → design → tests) and evidence collected during development and deployment.

---

## 2. What worked well (Strengths)

### 2.1 Architecture and separation of concerns
The solution follows a clear three-tier separation:
- presentation logic is separated from business rules,
- business rules are separated from persistence logic using DAO classes.

This improved maintainability because changes in the UI did not require rewriting business logic or SQL queries. It also improved testability since service methods could be unit tested independently of UI behavior.

### 2.2 Use of encapsulation and class design decisions
I used private fields with controlled access through getters and setters to enforce encapsulation and protect object invariants. This reduced the risk of invalid internal state (e.g., negative nights, missing reservation fields). Constructors were designed to support valid object initialization and clarity of object state at creation time (parameterized constructors) while still supporting required tooling or framework constraints (default constructors where needed).

### 2.3 Design patterns and their impact
The applied patterns improved the quality of the system:
- DAO reduced duplication of SQL code and isolated database responsibilities.
- Factory improved flexibility when constructing domain objects consistently.
- Strategy (billing rules) supported extension, such as introducing different room rate rules without rewriting billing logic.

The impact of these patterns was evaluated positively because they reduced coupling and supported incremental development under Unified Process iterations.

### 2.4 Validation robustness
Validation rules were applied both at the UI boundary and in the service layer to prevent invalid states from entering the system. This defensive approach is important in reservation systems because incorrect dates or invalid contact numbers can lead to billing errors and booking conflicts.

### 2.5 Testing evidence and quality assurance
Multiple testing strategies were selected to match the system’s risks:
- unit tests verified business rules quickly and reliably,
- integration tests validated database interactions and constraint behavior,
- system tests verified real user flows under a deployed environment.

This layered strategy improved confidence in correctness because it tested both internal logic and real end-to-end behavior.

---

## 3. Limitations and trade-offs (Critical analysis)

### 3.1 Simplicity of UI versus engineering focus
A deliberate trade-off was made to keep the UI simple because the primary assessment goal is advanced software engineering decisions and distributed Java EE design. A minimal UI reduced unnecessary complexity and allowed more effort to be focused on architecture, validation, patterns, and evidence.

### 3.2 Database design constraints
While the database design supports required storage and retrieval, advanced database features (e.g., triggers, stored procedures) were considered. However, implementing business rules at the application service layer was prioritised to keep rules centralised and testable using JUnit. This supports maintainability and reduces hidden logic in the database.

### 3.3 Deployment configuration complexity
Tomcat deployment and datasource configuration introduces environment complexity. However, this decision reflects real-world enterprise environments and supports the goal of demonstrating professional distributed system capability. The risk was mitigated through early environment validation and screenshot-based evidence.

---

## 4. Lessons learned

### Lesson 1 — Early architectural decisions reduce rework
Confirming the distributed architecture and database connectivity early prevented major rework later. This supports the Unified Process principle of addressing risk early, especially for deployment and persistence.

### Lesson 2 — Strong validation reduces downstream defects
Many potential defects (incorrect billing, booking conflicts) were prevented by strict input validation and service-layer checks. This showed that defensive programming improves system reliability.

### Lesson 3 — Testing strategy must match system risk
Choosing different testing strategies improved verification quality:
- unit tests catch logic errors early,
- integration tests catch boundary defects,
- system tests validate the real user experience and deployment setup.

### Lesson 4 — Evidence and traceability increase credibility
Maintaining traceability from requirements to tests and collecting screenshots throughout development made verification transparent and professionally presented.

---

## 5. Final judgement (overall success)
Overall, the system meets the core scenario requirements and demonstrates advanced software engineering practice through:
- a distributed design appropriate for Java EE and Tomcat,
- correct application of object-oriented principles and patterns,
- structured UP development and version control evidence,
- multi-layered testing with automation and documented proof.

The result is a maintainable, testable, and well-justified solution aligned with professional software engineering expectations.
