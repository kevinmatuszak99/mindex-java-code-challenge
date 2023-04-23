package com.mindex.challenge.data;

public class ReportingStructure {
    // Needs to have two properties: employee and numberofReports
    private Employee employee;
    private int numberOfReports;

    // No constructor needed
    public ReportingStructure() {
    }

    // Simple gets and sets
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
