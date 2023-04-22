package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.List;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.numberOfReports = calculateReports(employee);
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    private int calculateReports(Employee employee) {
        List<Employee> allReports = new ArrayList<>();
        List<Employee> reportsToProcess = new ArrayList<>(employee.getDirectReports());
        while(!reportsToProcess.isEmpty()) {
            Employee processingReport = reportsToProcess.get(0);
            for (Employee report : reportsToProcess) {
                if (!reportsToProcess.contains(report)) {
                    if (!allReports.contains(report)) {
                        reportsToProcess.add(report);
                    }
                }
            }
            reportsToProcess.addAll(processingReport.getDirectReports());
            allReports.add(processingReport);
            reportsToProcess.remove(0);
        }
        return allReports.size();
    }
}
