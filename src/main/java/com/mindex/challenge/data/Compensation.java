package com.mindex.challenge.data;

public class Compensation {
    // Needs to have three properties: employee, salary, and effectiveDate
    private Employee employee;
    private int salary;
    private String effectiveDate;

    // No constructor needed
    public Compensation() {
    }

    // Simple gets and sets
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
