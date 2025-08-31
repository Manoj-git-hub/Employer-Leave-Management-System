package com.example.lms.service;

import com.example.lms.dto.ApplyLeaveRequest;
import com.example.lms.dto.CreateEmployeeRequest;
import com.example.lms.exception.ApiException;
import com.example.lms.exception.NotFoundException;
import com.example.lms.model.Employee;
import com.example.lms.model.LeaveRequest;
import com.example.lms.model.LeaveStatus;
import com.example.lms.repository.EmployeeRepository;
import com.example.lms.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Value("${app.default-leave-balance:24}")
    private int defaultLeaveBalance;

    public LeaveService(EmployeeRepository employeeRepository, LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public Employee addEmployee(CreateEmployeeRequest req) {
        employeeRepository.findByEmail(req.getEmail()).ifPresent(e -> {
            throw new ApiException("Employee email already exists");
        });
        int balance = (req.getInitialLeaveBalance() != null) ? req.getInitialLeaveBalance() : defaultLeaveBalance;
        Employee e = new Employee(req.getName(), req.getEmail(), req.getDepartment(), req.getJoiningDate(), balance);
        return employeeRepository.save(e);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found"));
    }

    public int getLeaveBalance(Long employeeId) {
        return getEmployee(employeeId).getLeaveBalance();
    }

    @Transactional
    public LeaveRequest applyLeave(ApplyLeaveRequest req) {
        Employee e = getEmployee(req.getEmployeeId());
        LocalDate start = req.getStartDate();
        LocalDate end = req.getEndDate();

        if (end.isBefore(start)) throw new ApiException("End date cannot be before start date");
        if (start.isBefore(e.getJoiningDate())) throw new ApiException("Cannot apply leave before joining date");

        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;

        // Overlaps with pending/approved
        List<LeaveRequest> overlaps = leaveRequestRepository.findOverlapping(
                e.getId(), start, end, List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED));
        if (!overlaps.isEmpty()) throw new ApiException("Overlapping leave request exists");

        if (days > e.getLeaveBalance()) throw new ApiException("Insufficient leave balance");

        LeaveRequest lr = new LeaveRequest(e, start, end, req.getReason(), days);
        return leaveRequestRepository.save(lr);
    }

    @Transactional
    public LeaveRequest approve(Long leaveId) {
        LeaveRequest lr = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new NotFoundException("Leave request not found"));
        if (lr.getStatus() != LeaveStatus.PENDING) throw new ApiException("Only pending requests can be approved");

        Employee e = lr.getEmployee();
        if (lr.getDays() > e.getLeaveBalance()) throw new ApiException("Insufficient balance at approval time");

        e.setLeaveBalance(e.getLeaveBalance() - lr.getDays());
        lr.setStatus(LeaveStatus.APPROVED);
        employeeRepository.save(e);
        return leaveRequestRepository.save(lr);
    }

    @Transactional
    public LeaveRequest reject(Long leaveId) {
        LeaveRequest lr = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new NotFoundException("Leave request not found"));
        if (lr.getStatus() != LeaveStatus.PENDING) throw new ApiException("Only pending requests can be rejected");

        lr.setStatus(LeaveStatus.REJECTED);
        return leaveRequestRepository.save(lr);
    }
}
