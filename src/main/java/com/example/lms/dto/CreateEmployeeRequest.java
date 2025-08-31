package com.example.lms.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreateEmployeeRequest {

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String department;

    @NotNull
    private LocalDate joiningDate;

    @Min(0)
    private Integer initialLeaveBalance; // optional; defaults to config

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public Integer getInitialLeaveBalance() { return initialLeaveBalance; }
    public void setInitialLeaveBalance(Integer initialLeaveBalance) { this.initialLeaveBalance = initialLeaveBalance; }
}
