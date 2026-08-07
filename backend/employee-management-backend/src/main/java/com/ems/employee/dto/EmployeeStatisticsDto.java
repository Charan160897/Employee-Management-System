package com.ems.employee.dto;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeStatisticsDto {

    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private BigDecimal averageSalary;
    private long totalDepartments;
    private List<DepartmentCountDto> departmentCounts;

    public EmployeeStatisticsDto() {
    }

    public EmployeeStatisticsDto(
            long totalEmployees,
            long activeEmployees,
            long inactiveEmployees,
            BigDecimal averageSalary,
            long totalDepartments,
            List<DepartmentCountDto> departmentCounts
    ) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.inactiveEmployees = inactiveEmployees;
        this.averageSalary = averageSalary;
        this.totalDepartments = totalDepartments;
        this.departmentCounts = departmentCounts;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(
            long totalEmployees
    ) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(
            long activeEmployees
    ) {
        this.activeEmployees = activeEmployees;
    }

    public long getInactiveEmployees() {
        return inactiveEmployees;
    }

    public void setInactiveEmployees(
            long inactiveEmployees
    ) {
        this.inactiveEmployees = inactiveEmployees;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(
            BigDecimal averageSalary
    ) {
        this.averageSalary = averageSalary;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(
            long totalDepartments
    ) {
        this.totalDepartments = totalDepartments;
    }

    public List<DepartmentCountDto>
    getDepartmentCounts() {
        return departmentCounts;
    }

    public void setDepartmentCounts(
            List<DepartmentCountDto> departmentCounts
    ) {
        this.departmentCounts = departmentCounts;
    }
}