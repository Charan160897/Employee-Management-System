package com.ems.employee.service;

import com.ems.employee.dto.AuditLogResponseDto;

import java.util.List;

public interface AuditLogService {

    void log(
            String username,
            String action,
            String employeeName
    );

    List<AuditLogResponseDto>
    getAllLogs();
}