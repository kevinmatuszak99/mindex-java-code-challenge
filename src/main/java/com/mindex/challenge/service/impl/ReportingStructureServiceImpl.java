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

    @Override
    public ReportingStructure calculate(String id) {
        LOG.debug("Calculating employee reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(calculateReports(employee));

        return reportingStructure;
    }

    private int calculateReports(Employee employee) {
        List<Employee> allReports = new ArrayList<>();
        List<Employee> reportsToProcess = new ArrayList<>(employee.getDirectReports());
        while(!reportsToProcess.isEmpty()) {
            Employee processingReport = reportsToProcess.get(0);
            processingReport = employeeRepository.findByEmployeeId(processingReport.getEmployeeId());
            List<Employee> embeddedDirectReports = processingReport.getDirectReports();
            if (embeddedDirectReports != null) {
                for (Employee report : processingReport.getDirectReports()) {
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
}
