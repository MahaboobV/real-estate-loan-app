package com.task.loanapp.aws.request;

import com.task.loanapp.model.CustomerLoanDetails;

public class APIGatewayRequest {

    private String httpMethod;
    private CustomerLoanDetails customerLoanDetails;

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public CustomerLoanDetails getCustomerLoanDetails() {
        return customerLoanDetails;
    }

    public void setCustomerLoanDetails(CustomerLoanDetails customerLoanDetails) {
        this.customerLoanDetails = customerLoanDetails;
    }
}
