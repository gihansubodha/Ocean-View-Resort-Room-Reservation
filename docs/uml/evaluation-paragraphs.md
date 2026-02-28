4.1 Use Case Diagram Evaluation (1 paragraph)

The use case diagram models the complete functional scope requested in the scenario, including authentication, reservation creation, reservation retrieval, billing, help, and exit. Mandatory reusable behaviour (such as validation and bill sub-calculations) is represented using <<include>>, which reduces duplication and improves consistency across use cases. Optional or role-based functionality (such as reporting) is represented using <<extend>>, supporting incremental delivery under an iterative development approach. This use case model strengthens traceability from requirements to design, supporting a maintainable distributed Java EE system. 

ICBT_CIS6003_S1SRI_WRIT1_Nov-20…

4.2 Class Diagram Evaluation (1 paragraph)

The class design demonstrates object-oriented methodology by applying encapsulation (private fields with controlled access via getters and setters) to protect object state and prevent invalid reservation data. Responsibilities are separated into domain classes (Guest, Reservation, RoomType, Bill), service classes for business rules, and DAO classes for persistence, which aligns with a three-tier architecture and the Single Responsibility Principle. Multiplicity and relationships reflect business rules (many reservations per room type, optional bill generation per reservation), and DTOs reduce coupling between UI/API and the domain model. This design improves testability (service methods can be unit tested with JUnit) and supports future extension without destabilising core components. 

ICBT_CIS6003_S1SRI_WRIT1_Nov-20…

4.3 Sequence Diagram Evaluations (3 mini paragraphs)

Login sequence: The login sequence centralises authentication in a dedicated service rather than placing it in the UI or DAO. This separation of concerns improves security and maintainability by ensuring that credentials are handled consistently through a single entry point, and it supports distributed operation because multiple clients can reuse the same authentication service. The trade-off is an increased number of classes, but this is justified because it improves clarity, reuse, and professional alignment with enterprise Java EE design.

Add reservation sequence: The add-reservation sequence demonstrates defensive validation at both boundary and service levels before any database write occurs, preventing invalid states (such as incorrect dates or missing contact numbers) from entering persistence. DAO classes isolate SQL concerns from business rules, improving cohesion and reducing coupling. This layered flow provides strong traceability from the “Add Reservation” use case to concrete service and persistence responsibilities.

Generate bill sequence: The billing sequence isolates calculation logic within the service layer, ensuring the UI remains responsible only for presentation. Nights and total computation are deterministic and therefore highly suitable for TDD and automated unit testing. By retrieving room rates via DAO/database rather than hard-coding values, the bill reflects the single source of truth and supports maintainability if rates change, while keeping the client interface stable.