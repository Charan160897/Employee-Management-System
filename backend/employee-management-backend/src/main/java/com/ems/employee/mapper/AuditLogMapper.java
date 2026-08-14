package com.ems.employee.mapper;

import com.ems.employee.dto.AuditLogResponseDto;
import com.ems.employee.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponseDto toResponseDto(
            AuditLog auditLog
    ) {

        AuditLogResponseDto dto =
                new AuditLogResponseDto();

        dto.setId(
                auditLog.getId()
        );

        dto.setUsername(
                auditLog.getUsername()
        );

        dto.setAction(
                auditLog.getAction()
        );

        dto.setEmployeeName(
                auditLog.getEmployeeName()
        );

        dto.setTimestamp(
                auditLog.getTimestamp()
        );

        return dto;
    }
}