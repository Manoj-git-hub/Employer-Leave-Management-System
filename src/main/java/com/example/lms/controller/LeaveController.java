package com.example.lms.controller;

import com.example.lms.dto.ApplyLeaveRequest;
import com.example.lms.dto.EmployeeDTO;
import com.example.lms.model.LeaveRequest;
import com.example.lms.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> apply(@Valid @RequestBody ApplyLeaveRequest req) {
        LeaveRequest request = leaveService.applyLeave(req);
        return ResponseEntity.ok(convertToResponse(request));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Long id) {
        LeaveRequest request = leaveService.approve(id);
        return ResponseEntity.ok(convertToResponse(request));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> reject(@PathVariable Long id) {
        LeaveRequest request = leaveService.reject(id);
        return ResponseEntity.ok(convertToResponse(request));
    }

    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<?> balance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(Map.of(
                "employeeId", employeeId,
                "balance", leaveService.getLeaveBalance(employeeId)));
    }

    // 🔑 helper method to build clean JSON
    private Map<String, Object> convertToResponse(LeaveRequest request) {
        EmployeeDTO employeeDTO = new EmployeeDTO(
                request.getEmployee().getId(),
                request.getEmployee().getName(),
                request.getEmployee().getEmail(),
                request.getEmployee().getDepartment(),
                request.getEmployee().getJoiningDate(),
                request.getEmployee().getLeaveBalance());

        Map<String, Object> response = new HashMap<>();
        response.put("id", request.getId());
        response.put("employee", employeeDTO);
        response.put("startDate", request.getStartDate());
        response.put("endDate", request.getEndDate());
        response.put("status", request.getStatus());
        response.put("reason", request.getReason());
        response.put("days", request.getDays());

        return response;
    }
}