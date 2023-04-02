package com.task.loanapp.repository;

import com.task.loanapp.model.CustomerLoanEntity;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CustomerLoanRepository extends CrudRepository<CustomerLoanEntity, String> {

    List<CustomerLoanEntity> findAll();
    CustomerLoanEntity findByCustomerSSN(String customerSSN);

}
