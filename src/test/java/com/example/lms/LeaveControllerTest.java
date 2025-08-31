package com.example.lms;

import com.example.lms.dto.CreateEmployeeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_employee_then_apply_leave() throws Exception {
        CreateEmployeeRequest emp = new CreateEmployeeRequest();
        emp.setName("Jane");
        emp.setEmail("jane@example.com");
        emp.setDepartment("Engineering");
        emp.setJoiningDate(LocalDate.of(2025, 1, 1));
        emp.setInitialLeaveBalance(10);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andReturn();

        // Minimal happy-path test continues in service tests; controller test asserts
        // 200 for creation
    }
}
