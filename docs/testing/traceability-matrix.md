# Traceability Matrix (Requirements → Design → Testing Evidence)

## Purpose
This matrix provides traceability showing how each requirement is:
1) captured as a use case,
2) supported by UML design,
3) implemented using architectural/design patterns,
4) verified by test cases with evidence.

Traceability strengthens quality assurance by demonstrating that the design and testing directly satisfy the specification.

---

## RQ List (derived from assessment brief)
RQ1: User Authentication (Login)
RQ2: Add New Reservation (store full guest + booking details)
RQ3: Display Reservation Details (retrieve by reservation number)
RQ4: Calculate and Print Bill (nights × room rate)
RQ5: Help Section (guidelines for staff)
RQ6: Exit System (safe closure)
RQ7: Validation (restrict invalid entries)
RQ8: Distributed application with web services
RQ9: Use of appropriate design patterns
RQ10: Use of a proper database for information storage
RQ11: Reporting / value-added outputs (recommended)
RQ12: Testing: TDD + automation + evidence

---

## Traceability Table

| Requirement | Use Case (UC) | UML (Class/Seq) | Main Components (3-tier) | Patterns / Principles Evidence | Test Cases | Evidence to Attach |
|---|---|---|---|---|---|---|
| RQ1 Login | UC1 Login | Seq1 Login, User/Auth classes | UI → AuthService → UserDAO → DB | SRP (AuthService), Encapsulation (private fields), DAO | TC01–TC10 | UI/console login screenshot + DB user record + JUnit pass screenshot |
| RQ2 Add Reservation | UC2 Add Reservation | Seq2 Add Reservation, Reservation/Guest/RoomType | UI → ReservationService → ReservationDAO/GuestDAO | DAO, Factory (Reservation/Bill creation), Validation at boundaries | TC11–TC25 | Add reservation screenshot + phpMyAdmin inserted row + JUnit pass |
| RQ3 Display Reservation | UC3 View Reservation | Seq3 View Reservation, ReservationDTO | UI → ReservationService → ReservationDAO | DTO (reduces coupling), SRP | TC26–TC35 | View details screenshot + DB retrieval proof |
| RQ4 Billing | UC4 Generate Bill | Seq4 Generate Bill, BillCalculator | UI → BillingService → ReservationDAO/RoomTypeDAO | Strategy (billing rules by room type), Defensive programming | TC36–TC45 | Bill output screenshot + JUnit bill tests pass |
| RQ5 Help | UC5 Help | (Optional Seq) Help | UI only (or HelpService) | Usability principle (learnability), Clear messages | TC46 | Help page/console help screenshot |
| RQ6 Exit | UC6 Exit | (Optional Seq) Exit | UI → Resource cleanup | Resource management, reliability | TC47–TC48 | Exit screenshot + logs showing cleanup |
| RQ7 Validation | UC1–UC4 includes validation | Validation shown in Seq diagrams + service methods | UI + Service | Defensive programming, fail-fast validation | TC04–TC05, TC12–TC20, TC28, TC49–TC50 | Validation message screenshots + negative tests evidence |
| RQ8 Web services / Distributed | UC1–UC4 executed via services | Architecture diagram + service interaction sequences | Client → Web Service → Service → DAO → DB | 3-tier architecture, separation of concerns | TC01, TC11, TC26, TC41, TC50 | Tomcat deployment screenshot + endpoint proof |
| RQ9 Design patterns | Cross-cutting | Class diagram shows responsibilities | Business/Data layers | DAO, Factory, Strategy; SOLID/SRP justification | Indirect: TC suites | Screenshot of class structure + explanation paragraphs |
| RQ10 Database | UC2–UC4 depend on DB | ERD + mapping to classes | DAO layer | Integrity constraints; data consistency | TC09–TC10, TC21–TC22, TC29–TC30, TC44, TC48 | phpMyAdmin schema screenshot + sample data screenshot |
| RQ11 Reports | UC7 Reports (optional) | Seq for reports (optional) | UI → ReportService → DAO | SRP, cohesion; decision-making support | Add: TC51+ (if added) | Reports screenshot + query result screenshot |
| RQ12 TDD + automation | Applies to service layer | N/A | N/A | TDD cycle described + JUnit automation | TC suite | JUnit run screenshot + commit history showing test-first commits |

---

## Notes on how to use this matrix in the report
- Each requirement is linked to at least one use case and test case.
- Key screenshots are attached as evidence to demonstrate verification.
- This supports the rubric expectation of traceability and professional testing documentation.
