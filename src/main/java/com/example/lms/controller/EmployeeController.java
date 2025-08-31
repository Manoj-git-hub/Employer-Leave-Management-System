package com.example.lms.controller;

import com.example.lms.dto.EmployeeDTO;
import com.example.lms.model.Employee;
import com.example.lms.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create Employee
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody EmployeeDTO dto) {
        Employee emp = new Employee();
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        emp.setJoiningDate(dto.getJoiningDate());
        emp.setLeaveBalance(dto.getInitialLeaveBalance()); // ✅ fix here
        return ResponseEntity.ok(employeeRepository.save(emp));
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable Long id) {
        Optional<Employee> emp = employeeRepository.findById(id);
        return emp.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}