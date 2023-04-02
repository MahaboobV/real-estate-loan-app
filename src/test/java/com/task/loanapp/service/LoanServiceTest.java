package com.task.loanapp.service;

import com.task.loanapp.model.CustomerLoanDetails;
import com.task.loanapp.model.CustomerLoanEntity;
import com.task.loanapp.repository.CustomerLoanRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.atLeast;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomerLoanRepository customerLoanRepository;

    @InjectMocks
    private LoanService loanService;

    private static CustomerLoanDetails customerLoanDetails;

    private static CustomerLoanEntity customerLoanEntity;

    @BeforeAll
    static void setup() {
        // Given
        customerLoanEntity = new CustomerLoanEntity();
        customerLoanEntity.setId(UUID.randomUUID().toString());
        customerLoanEntity.setCustomerSSN("12345678910");
        customerLoanEntity.setFullName("Test User");
        customerLoanEntity.setLoanAmount(6000000);
        customerLoanEntity.setSalaryAmount(5000000);
        customerLoanEntity.setEquityAmount(1000000);

        customerLoanDetails = new CustomerLoanDetails();
        customerLoanDetails.setCustomerSSN("12345678910");
        customerLoanDetails.setFullName("Test User");
        customerLoanDetails.setLoanAmount(6000000);
        customerLoanDetails.setSalaryAmount(5000000);
        customerLoanDetails.setEquityAmount(1000000);

    }

    @Test
    void test_saveCustomerLoanDetails() {
        //When
        when(modelMapper.map(any(CustomerLoanDetails.class), eq(CustomerLoanEntity.class))).thenReturn(customerLoanEntity);
        when(customerLoanRepository.save(any(CustomerLoanEntity.class))).thenReturn(customerLoanEntity);

        loanService.saveCustomerLoanDetails(customerLoanDetails);

        //Then
        verify(modelMapper, times(1)).map(customerLoanDetails, CustomerLoanEntity.class);
        verify(customerLoanRepository, times(1)).save(customerLoanEntity);
    }

    @Test
    void test_getAllCustomerLoanDetails() {
        //When
        when(modelMapper.map(any(CustomerLoanEntity.class), eq(CustomerLoanDetails.class))).thenReturn(customerLoanDetails);
        when(customerLoanRepository.findAll()).thenReturn(List.of(customerLoanEntity));

        List<CustomerLoanDetails> actualCustomerLoanDetails = loanService.getAllCustomerLoanDetails();

        //Then
        verify(modelMapper, atLeast(1)).map(customerLoanEntity, CustomerLoanDetails.class);
        verify(customerLoanRepository, times(1)).findAll();

        assertEquals(actualCustomerLoanDetails.get(0), customerLoanDetails);
    }

    @Test
    void test_getSpecificCustomerLoanDetails() {
        //When
        when(modelMapper.map(any(CustomerLoanEntity.class), eq(CustomerLoanDetails.class))).thenReturn(customerLoanDetails);
        when(customerLoanRepository.findByCustomerSSN(anyString())).thenReturn(customerLoanEntity);

        CustomerLoanDetails actualCustomerLoanDetails = loanService.getCustomerLoanDetails("12345678910");

        //Then
        verify(modelMapper, times(1)).map(customerLoanEntity, CustomerLoanDetails.class);
        verify(customerLoanRepository, times(1)).findByCustomerSSN("12345678910");

        assertEquals(actualCustomerLoanDetails.getCustomerSSN(), customerLoanDetails.getCustomerSSN());
    }
}
