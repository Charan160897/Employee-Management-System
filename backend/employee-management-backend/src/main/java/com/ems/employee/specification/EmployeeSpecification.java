package com.ems.employee.specification;

import com.ems.employee.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> hasKeyword(
            String keyword
    ) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchValue =
                    "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("firstName")
                            ),
                            searchValue
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("lastName")
                            ),
                            searchValue
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("email")
                            ),
                            searchValue
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("jobTitle")
                            ),
                            searchValue
                    )
            );
        };
    }

    public static Specification<Employee> hasDepartment(
            String department
    ) {
        return (root, query, criteriaBuilder) -> {
            if (department == null || department.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(
                            root.get("department")
                    ),
                    department.trim().toLowerCase()
            );
        };
    }

    public static Specification<Employee> hasActiveStatus(
            Boolean active
    ) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("active"),
                    active
            );
        };
    }
}