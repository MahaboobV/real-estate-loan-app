package com.task.loanapp.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.task.loanapp.LoanApplication;
import com.task.loanapp.model.CustomerLoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = LoanApplication.class)
@WebAppConfiguration
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "amazon.dynamodb.endpoint=http://localhost:8000/",
        "amazon.aws.accesskey=testAccessKey",
        "amazon.aws.secretkey=testSecretKey" })
public class CustomerLoanRepositoryIntegrationTest {

    @Autowired
    CustomerLoanRepository customerLoanRepository;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @BeforeEach
    void setUp() {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        DescribeTableResult describeTableResult = amazonDynamoDB.describeTable("Customer-Loan");

        if(describeTableResult.getTable() == null) {
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(CustomerLoanEntity.class);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);
        }
        dynamoDBMapper.batchDelete(customerLoanRepository.findAll());
    }

    @Test
    void givenItem_whenRunFindAll_thenItemFound() {
        CustomerLoanEntity customerLoanEntity = new CustomerLoanEntity();
        customerLoanEntity.setId(UUID.randomUUID().toString());
        customerLoanEntity.setCustomerSSN("21139928914");
        customerLoanEntity.setFullName("Test User");
        customerLoanEntity.setEquityAmount(150000);
        customerLoanEntity.setSalaryAmount(5730000);
        customerLoanEntity.setLoanAmount(6000000);

        customerLoanRepository.save(customerLoanEntity);

        List<CustomerLoanEntity> resultList = (List<CustomerLoanEntity>) customerLoanRepository.findAll();

        assertTrue(resultList.size()>0 );

        assertEquals(resultList.get(0).getEquityAmount(), customerLoanEntity.getEquityAmount());

    }

}
