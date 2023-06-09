package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    // Calculates the ReportingStructure on the fly each time
    @Override
    public ReportingStructure calculate(String id) {
        LOG.debug("Calculating employee reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        Employee fullEmployee = restoreDirectReports(employee);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(fullEmployee);
        reportingStructure.setNumberOfReports(calculateReports(fullEmployee));

        return reportingStructure;
    }

    // Calculates how many reports the employee has
    private int calculateReports(Employee employee) {
        // Keep track of separate lists so that loops and overlaps are not
        // counted multiple times when calculating
        List<Employee> allReports = new ArrayList<>();
        List<Employee> reportsToProcess = new ArrayList<>();
        if (employee.getDirectReports() != null) {
            reportsToProcess.addAll(employee.getDirectReports());
        }
        while(!reportsToProcess.isEmpty()) {
            Employee processingReport = reportsToProcess.get(0);
            List<Employee> embeddedDirectReports = processingReport.getDirectReports();
            if (embeddedDirectReports != null) {
                for (Employee report : embeddedDirectReports) {
                    if (!reportsToProcess.contains(report)) {
                        if (!allReports.contains(report)) {
                            reportsToProcess.add(report);
                        }
                    }
                }
            }
            allReports.add(processingReport);
            reportsToProcess.remove(0);
        }
        return allReports.size();
    }

    // Recursive method to show full breadth of direct reports rather than just employee number
    private Employee restoreDirectReports(Employee employee) {
        if (employee.getDirectReports() != null) {
            List<Employee> restoredDirectReports = new ArrayList<>();
            for (Employee report : employee.getDirectReports()) {
                Employee fullReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
                restoredDirectReports.add(restoreDirectReports(fullReport));
            }
            employee.setDirectReports(restoredDirectReports);
        }
        return employee;
    }

}
