package com.ems.employee.Controller;

import com.ems.employee.dto.AuditLogResponseDto;
import com.ems.employee.service.AuditLogService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService
            auditLogService;

    public AuditLogController(
            AuditLogService auditLogService
    ) {
        this.auditLogService =
                auditLogService;
    }

    @GetMapping
    public List<AuditLogResponseDto>
    getAuditLogs() {

        return auditLogService
                .getAllLogs();
    }
}