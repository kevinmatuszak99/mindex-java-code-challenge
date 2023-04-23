package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        // Endpoint for reporting structure
        reportingStructureUrl = "http://localhost:" + port + "/reporting_structure/{id}";
    }

    @Test
    public void testReportingStructure() {
        // Create employee0 with no direct reports
        Employee testEmployee0 = new Employee();
        testEmployee0.setFirstName("John");
        testEmployee0.setLastName("Doe");
        testEmployee0.setDepartment("Engineering");
        testEmployee0.setPosition("Developer");

        Employee createdEmployee0 = restTemplate.postForEntity(employeeUrl, testEmployee0, Employee.class).getBody();

        // Create employee1 with employee0 as a direct report
        List<Employee> testEmployee1DirectReports = new ArrayList<>();
        testEmployee1DirectReports.add(createdEmployee0);

        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("Jane");
        testEmployee1.setLastName("Doe");
        testEmployee1.setDepartment("HR");
        testEmployee1.setPosition("Recruiter");
        testEmployee1.setDirectReports(testEmployee1DirectReports);

        Employee createdEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployee1, Employee.class).getBody();

        // Create employee2 with employee1 as a direct report
        List<Employee> testEmployee2DirectReports = new ArrayList<>();
        testEmployee2DirectReports.add(createdEmployee1);

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("First");
        testEmployee2.setLastName("Last");
        testEmployee2.setDepartment("Leadership");
        testEmployee2.setPosition("CEO");
        testEmployee2.setDirectReports(testEmployee2DirectReports);

        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();

        // Get the reporting structures and assert equivalence of number of
        // reports and employee in the reporting structures
        ReportingStructure reportingStructure2 = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdEmployee2.getEmployeeId()).getBody();
        assertEquals(2, reportingStructure2.getNumberOfReports());
        assertEmployeeEquivalence(createdEmployee2, reportingStructure2.getEmployee());

        ReportingStructure reportingStructure1 = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdEmployee1.getEmployeeId()).getBody();
        assertEquals(1, reportingStructure1.getNumberOfReports());
        assertEmployeeEquivalence(createdEmployee1, reportingStructure1.getEmployee());

        ReportingStructure reportingStructure0 = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdEmployee0.getEmployeeId()).getBody();
        assertEquals(0, reportingStructure0.getNumberOfReports());
        assertEmployeeEquivalence(createdEmployee0, reportingStructure0.getEmployee());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
