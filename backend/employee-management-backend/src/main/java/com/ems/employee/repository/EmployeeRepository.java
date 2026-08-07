package com.ems.employee.repository;

import com.ems.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    boolean existsByEmailIgnoreCase(
            String email
    );

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Long id
    );

    Page<Employee>
    findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            Pageable pageable
    );

    Page<Employee>
    findByDepartmentContainingIgnoreCase(
            String department,
            Pageable pageable
    );

    Page<Employee> findByActive(
            Boolean active,
            Pageable pageable
    );

    long countByActive(Boolean active);

    @Query("""
            SELECT COALESCE(AVG(e.salary), 0)
            FROM Employee e
            """)
    BigDecimal findAverageSalary();

    @Query("""
            SELECT COUNT(DISTINCT e.department)
            FROM Employee e
            """)
    long countDistinctDepartments();

    @Query("""
            SELECT e.department, COUNT(e)
            FROM Employee e
            GROUP BY e.department
            ORDER BY COUNT(e) DESC, e.department ASC
            """)
    List<Object[]> countEmployeesByDepartment();
}