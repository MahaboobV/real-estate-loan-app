package com.task.loanapp.controller;


import com.task.loanapp.model.CustomerLoanDetails;
import com.task.loanapp.service.LoanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.task.loanapp.constants.ApplicationConstants.CUSTOMER_LOAN_STORED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/loanapp")
public class LoanController {

    private static final Logger logger = LogManager.getLogger(LoanController.class);

    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping(path = "/test", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testController() {
        return new ResponseEntity<>("Rest Controller Pinging....!", HttpStatus.OK);
    }

    @GetMapping(path = "/allcustomerloandetails", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerLoanDetails>> getAllCustomerLoanDetails() {
        List<CustomerLoanDetails> allCustomerLoanDetailsList = loanService.getAllCustomerLoanDetails();

        return new ResponseEntity<>(allCustomerLoanDetailsList, HttpStatus.OK);
    }

    @GetMapping(path = "/customerloandetails", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerLoanDetails> getCustomerLoanDetails(@RequestParam(name = "customerSSN") @NotNull @Max(11) String customerSSN) {
        CustomerLoanDetails customerLoanDetails = loanService.getCustomerLoanDetails(customerSSN);

        return new ResponseEntity<>(customerLoanDetails, HttpStatus.OK);
    }

    @PostMapping(path = "/save/customerloandetails", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> storeCustomerLoanDetails(@RequestBody CustomerLoanDetails customerLoanDetails) {
        loanService.saveCustomerLoanDetails(customerLoanDetails);

        return new ResponseEntity<>(CUSTOMER_LOAN_STORED, HttpStatus.OK);
    }
}
