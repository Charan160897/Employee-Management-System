package com.ems.employee.dto;

public class DepartmentCountDto {

    private String department;
    private long employeeCount;

    public DepartmentCountDto() {
    }

    public DepartmentCountDto(
            String department,
            long employeeCount
    ) {
        this.department = department;
        this.employeeCount = employeeCount;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department
    ) {
        this.department = department;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(
            long employeeCount
    ) {
        this.employeeCount = employeeCount;
    }
}