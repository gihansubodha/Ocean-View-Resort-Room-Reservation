# Test Cases (50) — Ocean View Resort Reservation System

## Legend
Type: UT = Unit Test, IT = Integration Test, ST = System Test
Priority: H/M/L

---

## A. Authentication (10 test cases)

TC01 (ST, H): Valid login
- Input: correct username + correct password
- Expected: user logged in, access granted

TC02 (ST, H): Invalid password
- Input: correct username + wrong password
- Expected: login denied, error message shown

TC03 (ST, H): Invalid username
- Input: wrong username + any password
- Expected: login denied, error message shown

TC04 (ST, H): Empty username
- Input: empty username + valid password
- Expected: validation message, no request processed

TC05 (ST, H): Empty password
- Input: valid username + empty password
- Expected: validation message, no request processed

TC06 (UT, M): Password policy validation (if enforced)
- Input: password shorter than required length
- Expected: validation fails with message

TC07 (UT, M): Username trimming
- Input: username with leading/trailing spaces
- Expected: spaces trimmed; correct decision applied

TC08 (ST, M): Multiple failed attempts handling (if implemented)
- Input: 5 invalid attempts
- Expected: account temporarily locked or warning shown (document behavior)

TC09 (IT, H): Auth user retrieval from DB
- Input: login request for existing user
- Expected: DB query returns correct user record

TC10 (IT, H): Auth with DB offline
- Input: login while DB service stopped
- Expected: graceful error handling, no system crash

---

## B. Add Reservation (Validation + Create) (15 test cases)

TC11 (ST, H): Add reservation with valid details
- Input: all valid fields
- Expected: reservation saved; confirmation with reservation number

TC12 (ST, H): Missing guest name
- Input: empty guest name
- Expected: validation error

TC13 (ST, H): Missing contact number
- Input: empty contact number
- Expected: validation error

TC14 (ST, H): Invalid contact number letters
- Input: contact = "07ABCD1234"
- Expected: validation error

TC15 (ST, M): Contact number too short
- Input: contact length < expected
- Expected: validation error

TC16 (ST, M): Contact number too long
- Input: contact length > expected
- Expected: validation error

TC17 (ST, H): Invalid date order
- Input: check-out earlier than check-in
- Expected: validation error; reservation not saved

TC18 (UT, H): Same-day check-in/check-out rule
- Input: check-in equals check-out
- Expected: should fail or should pass based on your rule; justify decision

TC19 (ST, M): Invalid date format
- Input: date in wrong format
- Expected: validation error

TC20 (ST, M): Invalid room type
- Input: room type not in allowed set
- Expected: validation error

TC21 (IT, H): DB insert reservation success
- Input: valid reservation object
- Expected: row inserted; correct fields stored

TC22 (IT, H): Reservation number uniqueness
- Input: attempt to store duplicate reservation number
- Expected: rejected by validation/DB constraint; error returned

TC23 (ST, H): Overbooking prevention (if implemented)
- Input: reservation for occupied room/time range
- Expected: system prevents booking conflict

TC24 (ST, M): Long address boundary
- Input: very long address (max allowed length)
- Expected: accepted up to limit; rejected beyond limit

TC25 (ST, M): Special characters in name/address
- Input: name "Anne-Marie", address with commas
- Expected: accepted and stored correctly

---

## C. View / Display Reservation (10 test cases)

TC26 (ST, H): View reservation by existing reservation number
- Input: valid reservation number
- Expected: full details displayed (guest + dates + room type)

TC27 (ST, H): View reservation with non-existing number
- Input: unknown reservation number
- Expected: “Not found” message

TC28 (ST, M): Empty reservation number search
- Input: empty
- Expected: validation message

TC29 (IT, H): DB retrieval mapping correctness
- Input: fetch reservation
- Expected: fields correctly mapped to object (no swapped dates)

TC30 (IT, M): DB retrieval with special characters
- Input: record with special characters
- Expected: displayed correctly (no encoding issues)

TC31 (ST, M): Unauthorized access to view (if roles)
- Input: user without permission tries to view
- Expected: access denied (or not applicable; justify)

TC32 (ST, M): View after update (if update supported)
- Input: update then view
- Expected: latest values shown

TC33 (ST, M): View after delete (if delete supported)
- Input: delete then view
- Expected: not found

TC34 (UT, M): Formatting of displayed dates
- Input: reservation dates
- Expected: consistent date format output

TC35 (ST, L): Performance sanity test (small dataset)
- Input: search in dataset of N records
- Expected: responds in acceptable time (describe)

---

## D. Billing (10 test cases)

TC36 (UT, H): Nights calculation standard
- Input: check-in 2026-01-01, check-out 2026-01-03
- Expected: nights = 2

TC37 (UT, H): Nights calculation invalid order
- Input: check-in after check-out
- Expected: throws validation error / returns failure

TC38 (UT, H): Bill calculation SINGLE room
- Input: nights = 2, rate = 100
- Expected: total = 200

TC39 (UT, H): Bill calculation DOUBLE room
- Input: nights = 3, rate = 150
- Expected: total = 450

TC40 (UT, M): Bill calculation with discount (if implemented)
- Input: nights threshold qualifies
- Expected: discount applied correctly

TC41 (ST, H): Generate bill for existing reservation
- Input: reservation number
- Expected: bill output shows nights, rate, total

TC42 (ST, H): Generate bill for non-existing reservation
- Input: invalid reservation number
- Expected: error message

TC43 (ST, M): Bill output formatting
- Input: normal bill
- Expected: readable “print-like” output

TC44 (IT, M): Bill uses correct room rate from DB
- Input: room type rate changed in DB
- Expected: bill reflects updated rate

TC45 (ST, M): Large total handling
- Input: long stay (e.g., 60 nights)
- Expected: correct computation, no overflow/format issues

---

## E. Help, Exit, and Robustness (5 test cases)

TC46 (ST, L): Help section displays correctly
- Input: open help
- Expected: guidelines shown clearly

TC47 (ST, L): Exit action closes safely
- Input: exit
- Expected: system closes without errors; resources released

TC48 (IT, H): Resource cleanup (DB connection returned/closed)
- Input: run multiple operations
- Expected: no connection leaks; pool/close is correct

TC49 (ST, M): SQL injection style input handling
- Input: name = "' OR '1'='1"
- Expected: treated as plain text; no unauthorized access/data corruption

TC50 (ST, M): System error handling message quality
- Input: force error (DB down)
- Expected: friendly message + logged technical detail (not exposed to user)
