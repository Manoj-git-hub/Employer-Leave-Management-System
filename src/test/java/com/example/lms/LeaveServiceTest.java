package com.example.lms;

import com.example.lms.dto.ApplyLeaveRequest;
import com.example.lms.dto.CreateEmployeeRequest;
import com.example.lms.exception.ApiException;
import com.example.lms.model.LeaveRequest;
import com.example.lms.service.LeaveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
public class LeaveServiceTest {

    @Autowired
    private LeaveService leaveService;

    private Long seedEmployee() {
        CreateEmployeeRequest emp = new CreateEmployeeRequest();
        emp.setName("John");
        emp.setEmail("john@example.com");
        emp.setDepartment("QA");
        emp.setJoiningDate(LocalDate.of(2025, 1, 1));
        emp.setInitialLeaveBalance(5);
        return leaveService.addEmployee(emp).getId();
    }

    @Test
    void apply_and_approve_happy_flow() {
        Long empId = seedEmployee();

        ApplyLeaveRequest req = new ApplyLeaveRequest();
        req.setEmployeeId(empId);
        req.setStartDate(LocalDate.of(2025, 1, 2));
        req.setEndDate(LocalDate.of(2025, 1, 3));
        req.setReason("Trip");

        LeaveRequest lr = leaveService.applyLeave(req);
        Assertions.assertNotNull(lr.getId());

        LeaveRequest approved = leaveService.approve(lr.getId());
        Assertions.assertEquals(2, approved.getDays());
        Assertions.assertEquals("APPROVED", approved.getStatus().name());
        Assertions.assertEquals(3, leaveService.getLeaveBalance(empId));
    }

    @Test
    void reject_overlapping() {
        Long empId = seedEmployee();

        ApplyLeaveRequest req1 = new ApplyLeaveRequest();
        req1.setEmployeeId(empId);
        req1.setStartDate(LocalDate.of(2025, 1, 2));
        req1.setEndDate(LocalDate.of(2025, 1, 4));
        leaveService.applyLeave(req1);

        ApplyLeaveRequest req2 = new ApplyLeaveRequest();
        req2.setEmployeeId(empId);
        req2.setStartDate(LocalDate.of(2025, 1, 3));
        req2.setEndDate(LocalDate.of(2025, 1, 5));

        Assertions.assertThrows(ApiException.class, () -> leaveService.applyLeave(req2));
    }

    @Test
    void invalid_dates_and_joining_date() {
        Long empId = seedEmployee();

        ApplyLeaveRequest bad = new ApplyLeaveRequest();
        bad.setEmployeeId(empId);
        bad.setStartDate(LocalDate.of(2024, 12, 30));
        bad.setEndDate(LocalDate.of(2024, 12, 31));
        Assertions.assertThrows(ApiException.class, () -> leaveService.applyLeave(bad));

        ApplyLeaveRequest badOrder = new ApplyLeaveRequest();
        badOrder.setEmployeeId(empId);
        badOrder.setStartDate(LocalDate.of(2025, 1, 10));
        badOrder.setEndDate(LocalDate.of(2025, 1, 9));
        Assertions.assertThrows(ApiException.class, () -> leaveService.applyLeave(badOrder));
    }

    @Test
    void insufficient_balance() {
        Long empId = seedEmployee();

        ApplyLeaveRequest req = new ApplyLeaveRequest();
        req.setEmployeeId(empId);
        req.setStartDate(LocalDate.of(2025, 1, 2));
        req.setEndDate(LocalDate.of(2025, 1, 10)); // 9 days > 5
        Assertions.assertThrows(ApiException.class, () -> leaveService.applyLeave(req));
    }
}
