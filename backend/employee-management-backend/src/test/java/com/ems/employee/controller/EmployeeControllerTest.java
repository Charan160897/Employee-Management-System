package com.ems.employee.controller;

import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.exception.EmployeeNotFoundException;
import com.ems.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @MockitoBean
    private EmployeeService employeeService;


    @Test
    void getEmployeeById_shouldReturn200()
            throws Exception {

        EmployeeResponseDto employee =
                new EmployeeResponseDto();

        employee.setId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("Kumar");
        employee.setFullName("Rahul Kumar");
        employee.setEmail("rahul@example.com");
        employee.setDepartment("Engineering");
        employee.setJobTitle("Backend Developer");

        employee.setSalary(
                BigDecimal.valueOf(90000)
        );

        employee.setHireDate(
                LocalDate.of(2026, 7, 28)
        );

        employee.setActive(true);

        when(
                employeeService.getEmployeeById(1L)
        ).thenReturn(employee);

        mockMvc.perform(
                        get("/api/employees/1")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.firstName")
                                .value("Rahul")
                )
                .andExpect(
                        jsonPath("$.department")
                                .value("Engineering")
                );
    }


    @Test
    void getEmployeeById_shouldReturn404WhenMissing()
            throws Exception {

        when(
                employeeService.getEmployeeById(999L)
        ).thenThrow(
                new EmployeeNotFoundException(999L)
        );

        mockMvc.perform(
                        get("/api/employees/999")
                )
                .andExpect(
                        status().isNotFound()
                );
    }
}