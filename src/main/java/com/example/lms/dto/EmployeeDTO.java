package com.example.lms.dto;

import java.time.LocalDate;

public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private String department;
    private LocalDate joiningDate;
    private int initialLeaveBalance; // ✅ fix here

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String name, String email, String department, LocalDate joiningDate,
            int initialLeaveBalance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.joiningDate = joiningDate;
        this.initialLeaveBalance = initialLeaveBalance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public int getInitialLeaveBalance() {
        return initialLeaveBalance;
    }

    public void setInitialLeaveBalance(int initialLeaveBalance) {
        this.initialLeaveBalance = initialLeaveBalance;
    }
}
