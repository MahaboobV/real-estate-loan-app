package com.task.loanapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.loanapp.config.SecurityConfig;
import com.task.loanapp.model.CustomerLoanDetails;
import com.task.loanapp.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
@WebMvcTest(LoanController.class)
public class CustomerLoanControllerTest {

    @MockBean
    private LoanService loanService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;;

    private static List<CustomerLoanDetails> customerLoanDetailsList = new ArrayList<>();

    @BeforeEach
    void setup() {
        CustomerLoanDetails customerLoanDetails = new CustomerLoanDetails();
        customerLoanDetails.setCustomerSSN("21139928914");
        customerLoanDetails.setFullName("Test User");
        customerLoanDetails.setEquityAmount(150000);
        customerLoanDetails.setSalaryAmount(5730000);
        customerLoanDetails.setLoanAmount(6000000);
        customerLoanDetailsList.add(customerLoanDetails);
    }


    @Test
    void testGetAllCustomerLoanDetails() throws Exception {
        //When
        when(loanService.getAllCustomerLoanDetails()).thenReturn(customerLoanDetailsList);

        MvcResult mvcResult = mockMvc.perform(get("/loanapp/allcustomerloandetails")
                             .with(SecurityMockMvcRequestPostProcessors.jwt())).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        List<CustomerLoanDetails> actualCustomerLoanDetails = objectMapper.readValue(result, new TypeReference<>() {});

        //Then
        assertEquals(actualCustomerLoanDetails.size(), customerLoanDetailsList.size());

        assertEquals(actualCustomerLoanDetails.get(0).getCustomerSSN(), customerLoanDetailsList.get(0).getCustomerSSN());

    }

    @Test
    void testGetIndividualCustomerLoanDetails() throws Exception {

        //When
        when(loanService.getCustomerLoanDetails(anyString())).thenReturn(customerLoanDetailsList.get(0));

        MvcResult mvcResult = mockMvc.perform(get("/loanapp/customerloandetails").param("customerSSN", "12345678910")
                .with(SecurityMockMvcRequestPostProcessors.jwt())).andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        CustomerLoanDetails actualCustomerLoanDetails = objectMapper.readValue(result, CustomerLoanDetails.class);

        //Then
        assertEquals(actualCustomerLoanDetails, customerLoanDetailsList.get(0));
    }

    @Test
    void testStoreCustomerLoanDetails() throws Exception {
        //Given
        String requestJson = objectMapper.writeValueAsString(customerLoanDetailsList.get(0));

        //When
        doNothing().when(loanService).saveCustomerLoanDetails(any(CustomerLoanDetails.class));

        mockMvc.perform(post("/loanapp/save/customerloandetails").content(requestJson).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.jwt())).andExpect(status().isOk());
        //Then
       verify(loanService, times(1)).saveCustomerLoanDetails(customerLoanDetailsList.get(0));
    }

}
