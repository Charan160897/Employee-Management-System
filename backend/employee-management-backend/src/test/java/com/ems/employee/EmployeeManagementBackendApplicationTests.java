package com.ems.employee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "jwt.secret=MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=",
                "jwt.expiration=3600000"
        }
)
class EmployeeManagementBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}