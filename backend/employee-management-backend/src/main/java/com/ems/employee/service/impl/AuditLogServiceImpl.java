package com.ems.employee.service.impl;

import com.ems.employee.dto.AuditLogResponseDto;
import com.ems.employee.entity.AuditLog;
import com.ems.employee.mapper.AuditLogMapper;
import com.ems.employee.repository.AuditLogRepository;
import com.ems.employee.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl
        implements AuditLogService {

    private final AuditLogRepository
            auditLogRepository;

    private final AuditLogMapper
            auditLogMapper;

    public AuditLogServiceImpl(
            AuditLogRepository auditLogRepository,
            AuditLogMapper auditLogMapper
    ) {
        this.auditLogRepository =
                auditLogRepository;

        this.auditLogMapper =
                auditLogMapper;
    }

    @Override
    public void log(
            String username,
            String action,
            String employeeName
    ) {

        AuditLog auditLog =
                new AuditLog();

        auditLog.setUsername(
                username
        );

        auditLog.setAction(
                action
        );

        auditLog.setEmployeeName(
                employeeName
        );

        auditLog.setTimestamp(
                LocalDateTime.now()
        );

        auditLogRepository.save(
                auditLog
        );
    }

    @Override
    public List<AuditLogResponseDto>
    getAllLogs() {

        return auditLogRepository
                .findAllByOrderByTimestampDesc()
                .stream()
                .map(
                        auditLogMapper
                                ::toResponseDto
                )
                .toList();
    }
}