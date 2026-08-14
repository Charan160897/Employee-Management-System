package com.ems.employee.repository;

import com.ems.employee.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog>
    findAllByOrderByTimestampDesc();
}