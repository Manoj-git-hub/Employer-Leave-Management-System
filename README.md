# Mini Leave Management System (Spring Boot)

## Setup
1. Ensure you have Java 17 and Maven installed.
2. Run:
   ```bash
   mvn spring-boot:run
   ```

The app uses an in-memory H2 database. H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:lmsdb`).

## API Endpoints (JSON)
### Employees
- **POST** `/api/employees`
  ```json
  {
    "name": "Alice",
    "email": "alice@example.com",
    "department": "Engineering",
    "joiningDate": "2025-01-15",
    "initialLeaveBalance": 24
  }
  ```
- **GET** `/api/employees/{id}`
- **GET** `/api/employees`

### Leave
- **POST** `/api/leaves/apply`
  ```json
  {
    "employeeId": 1,
    "startDate": "2025-02-10",
    "endDate": "2025-02-12",
    "reason": "Family function"
  }
  ```
- **POST** `/api/leaves/{leaveId}/approve`
- **POST** `/api/leaves/{leaveId}/reject`
- **GET** `/api/leaves/balance/{employeeId}`

## Edge Cases Handled
- Employee not found
- End date before start date
- Leave before joining date
- Applying for more days than available balance
- Overlapping leave requests (against PENDING/APPROVED)
- Duplicate employee email (unique constraint)

## Assumptions
- Leave days are **calendar days** (inclusive). Weekends/holidays not excluded.
- Balance deducted **on approval** (not on application).
- Default leave balance is 24 if not provided at employee creation.

## Potential Improvements
- Role-based auth (HR vs Employee)
- Business-day calculation + holidays calendar
- Partial-day leave
- Different leave types and accrual rules
- Email/notification integration
- Pagination & filters for lists
