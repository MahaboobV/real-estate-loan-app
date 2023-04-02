package com.task.loanapp;

import com.task.loanapp.controller.LoanController;
import com.task.loanapp.repository.CustomerLoanRepository;
import com.task.loanapp.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LoanAppTestApplication {

    @Autowired
    private LoanController loanController;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    @Test
    public void contextLoads() {
        assertNotNull(loanController);
        assertNotNull(loanService);
        assertNotNull(customerLoanRepository);

    }
}
