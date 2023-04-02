package com.task.loanapp.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.loanapp.model.CustomerLoanDetails;
import com.task.loanapp.model.TokenResponse;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
public class CommonController {

    private static final Logger logger = LogManager.getLogger(CommonController.class);

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.cognito.client-secret}")
    private String clientSecret;


    @Value("${aws_access_key_id}")
    private String accessKey;

    @Value("${aws_secret_access_key}")
    private String accessSecret;

    @Value("${aws_region}")
    private String awsRegion;

    @Value("${aws_token_url}")
    private String awsTokenUrl;

    @Value("${aws_user_pool}")
    private String awsUserpool;

    public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, accessSecret);
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(awsRegion)
                .build();
    }

    @RequestMapping("/getToken")
    public @ResponseBody TokenResponse getJwtToken(@RequestParam(name = "code") String code) throws Exception {

        String credential = clientId + ":" + clientSecret;
        String encodedCredentails = new String(Base64.getEncoder().encode(credential.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", "Basic " + encodedCredentails);

        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
        String accessTokenUrl = awsTokenUrl;
        accessTokenUrl += "?code=" + code;
        accessTokenUrl += "&grant_type=authorization_code";
        accessTokenUrl += "&redirect_uri=http://localhost:8090/index.html";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, httpEntity, String.class);
        logger.info("Access Token Response ---------{}", response.getBody());
        //System.out.println("Access Token Response ---------" + response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String token = jsonNode.path("id_token").asText();

        String[] chunks = token.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        String viewName = null;
        JSONObject headerObj = objectMapper.readValue(header, JSONObject.class);
        JSONObject payLoadObj = objectMapper.readValue(payload, JSONObject.class);
        if (payLoadObj.get("cognito:username").equals("loan-app-user")) {
            viewName = "customer";
        }else if(payLoadObj.get("cognito:username").equals("loan-app-adviser")) {
            viewName = "adviser";
        }
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        tokenResponse.setViewName(viewName);
        return tokenResponse;

    }
   @RequestMapping("/customerloanform")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("customer-loan-application");
        modelAndView.addObject("customerloandetails", new CustomerLoanDetails());
        return modelAndView;
    }
}
