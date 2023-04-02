package com.task.loanapp.aws.function;

import com.task.loanapp.aws.request.APIGatewayRequest;
import com.task.loanapp.model.CustomerLoanEntity;
import com.task.loanapp.repository.CustomerLoanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.function.Function;

public class CreateCustomerLoanFunction implements Function<APIGatewayRequest, CustomerLoanEntity> {

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CustomerLoanEntity apply(APIGatewayRequest apiGatewayRequest) {
       CustomerLoanEntity customerLoanEntity = modelMapper.map(apiGatewayRequest.getCustomerLoanDetails(), CustomerLoanEntity.class);
        customerLoanEntity.setId(UUID.randomUUID().toString());
        customerLoanRepository.save(customerLoanEntity);
        return customerLoanEntity;
    }
}
