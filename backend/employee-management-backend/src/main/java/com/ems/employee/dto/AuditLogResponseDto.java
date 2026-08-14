package com.ems.employee.dto;

import java.time.LocalDateTime;

public class AuditLogResponseDto {

    private Long id;
    private String username;
    private String action;
    private String employeeName;
    private LocalDateTime timestamp;

    public AuditLogResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(
            String employeeName
    ) {
        this.employeeName = employeeName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            LocalDateTime timestamp
    ) {
        this.timestamp = timestamp;
    }
}