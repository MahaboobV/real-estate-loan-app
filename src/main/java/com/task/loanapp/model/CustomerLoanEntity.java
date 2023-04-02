package com.task.loanapp.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Customer-Loan")
public class CustomerLoanEntity {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute(attributeName = "customer_ssn")
    private String customerSSN;


    @DynamoDBAttribute(attributeName = "full_name")
    private String fullName;

    @DynamoDBAttribute(attributeName = "loan_amount")
    private long loanAmount;

    @DynamoDBAttribute(attributeName = "equity_amount")
    private long equityAmount;

    @DynamoDBAttribute(attributeName = "salary_amount")
    private long salaryAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public long getEquityAmount() {
        return equityAmount;
    }

    public void setEquityAmount(long equityAmount) {
        this.equityAmount = equityAmount;
    }

    public long getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(long salaryAmount) {
        this.salaryAmount = salaryAmount;
    }
}
