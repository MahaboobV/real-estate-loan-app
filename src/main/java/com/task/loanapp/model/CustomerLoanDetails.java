package com.task.loanapp.model;

import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CustomerLoanDetails {
    @NotBlank
    @NotNull
    private String customerSSN;
    private String fullName;
    private double loanAmount;
    private double equityAmount;
    private double salaryAmount;

    public CustomerLoanDetails() {
    }

    public CustomerLoanDetails(String customerSSN, String fullName, double loanAmount,
                               double equityAmount, double salaryAmount) {
        this.customerSSN = customerSSN;
        this.fullName = fullName;
        this.loanAmount = loanAmount;
        this.equityAmount = equityAmount;
        this.salaryAmount = salaryAmount;
    }

    public String getCustomerSSN() {
        return customerSSN;
    }

    public void setCustomerSSN(String customerSSN) {
        this.customerSSN = customerSSN;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getEquityAmount() {
        return equityAmount;
    }

    public void setEquityAmount(double equityAmount) {
        this.equityAmount = equityAmount;
    }

    public double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerLoanDetails{");
        sb.append("customerSSN='").append(customerSSN).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", loanAmount=").append(loanAmount);
        sb.append(", equityAmount=").append(equityAmount);
        sb.append(", salaryAmount=").append(salaryAmount);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerLoanDetails that = (CustomerLoanDetails) o;
        return Double.compare(that.loanAmount, loanAmount) == 0 && Double.compare(that.equityAmount, equityAmount) == 0 && Double.compare(that.salaryAmount, salaryAmount) == 0 && customerSSN.equals(that.customerSSN) && fullName.equals(that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerSSN, fullName, loanAmount, equityAmount, salaryAmount);
    }
}
