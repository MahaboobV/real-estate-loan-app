package com.task.loanapp.service;

import com.task.loanapp.exception.ResourceNotFoundException;
import com.task.loanapp.model.CustomerLoanDetails;
import com.task.loanapp.model.CustomerLoanEntity;
import com.task.loanapp.repository.CustomerLoanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.task.loanapp.constants.ApplicationConstants.CUSTOMER_LOAN_DETAILS_NOT_FOUND;
import static com.task.loanapp.constants.ApplicationConstants.CUSTOMER_LOAN_DETAILS_SSN_NOT_FOUND;

@Service
public class LoanService {

    private final CustomerLoanRepository customerLoanRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public LoanService(CustomerLoanRepository customerLoanRepository, ModelMapper modelMapper) {
        this.customerLoanRepository = customerLoanRepository;
        this.modelMapper = modelMapper;
    }

    public void saveCustomerLoanDetails(CustomerLoanDetails customerLoanDetails) {
        CustomerLoanEntity customerLoanEntity = modelMapper.map(customerLoanDetails, CustomerLoanEntity.class);
        customerLoanEntity.setId(UUID.randomUUID().toString());
        customerLoanRepository.save(customerLoanEntity);
    }

    public CustomerLoanDetails getCustomerLoanDetails(String customerSSN) {
        CustomerLoanEntity customerLoanEntity = customerLoanRepository.findByCustomerSSN(customerSSN);
        if(null == customerLoanEntity) {
            throw new ResourceNotFoundException(CUSTOMER_LOAN_DETAILS_SSN_NOT_FOUND+customerSSN);
        }
        return modelMapper.map(customerLoanEntity, CustomerLoanDetails.class);
    }

    public List<CustomerLoanDetails> getAllCustomerLoanDetails() {
        List<CustomerLoanEntity> customerLoanEntities = customerLoanRepository.findAll();
        if(customerLoanEntities.isEmpty()) {
                throw new ResourceNotFoundException(CUSTOMER_LOAN_DETAILS_NOT_FOUND);
        }
        List<CustomerLoanDetails> customerLoanDetailsList = new ArrayList<>();
        customerLoanEntities.forEach(customerLoanEntity -> customerLoanDetailsList.add(modelMapper.map(customerLoanEntity, CustomerLoanDetails.class)));
        return customerLoanDetailsList;
    }
}
