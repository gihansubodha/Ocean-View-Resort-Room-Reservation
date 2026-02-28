Encapsulation / access modifiers

“I used private fields to enforce encapsulation and prevent external classes from violating invariants; access is provided through getters/setters to control validation and maintain class integrity.”

Constructors

“I used a default constructor to support frameworks/serialization needs and a parameterized constructor to guarantee valid object initialization with required fields.”

Collections choice

“I used ArrayList for ordered iteration and fast indexed access, and HashMap for fast lookup by reservation number (average O(1)), which suits frequent search operations.”

DAO + layering

“DAO isolates persistence logic from business logic, supporting single responsibility and enabling unit testing with stubs/mocks.”

Validation

“Input validation is enforced at UI boundary and service layer to reduce invalid state propagation (defensive programming).”