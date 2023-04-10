package com.task.loanapp.oauth.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomValidator implements OAuth2TokenValidator<Jwt> {

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String clientId;

    @Value("${aws_user_pool}")
    private String userPool;

    OAuth2Error error = new OAuth2Error("invalid_token", "Invalid Token", null);

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (!jwt.getClaim("aud").equals(clientId)) {
            return OAuth2TokenValidatorResult.failure(error);
        }
        if (!jwt.getClaim("iss").equals(userPool)) {
            return OAuth2TokenValidatorResult.failure(error);
        }  if (!jwt.getClaim("token_use").equals("id")) {
            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}
