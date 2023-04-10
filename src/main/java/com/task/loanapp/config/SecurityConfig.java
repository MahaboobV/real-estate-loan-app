package com.task.loanapp.config;

import com.task.loanapp.oauth.validator.CustomValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // To configure method-level security
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.client.provider.cognito.issuerUri}")
    private String issuerUri;

    private static final String AUTHORITY_PREFIX = "ROLE_";
    private static final String CLAIM_ROLES = "roles";

    /*@Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .and()
                .authorizeRequests(auth -> auth.antMatchers("/index*").permitAll()
                        .antMatchers("/getToken").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login()
                .and()
                .logout()
                .logoutSuccessUrl("/");
        return http.build();
    }*/


    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().and()
                .cors().disable()
                .authorizeRequests(authorize -> authorize
                        .mvcMatchers(HttpMethod.GET, "/loanapp/test").hasAuthority("loan-app-test")
                        .mvcMatchers(HttpMethod.GET, "/loanapp/allcustomerloandetails").hasAuthority("loan-app-adviser")
                        .mvcMatchers(HttpMethod.POST, "/loanapp/save/customerloandetails").hasAuthority("loan-app-customer")
                        .mvcMatchers(HttpMethod.GET, "/loanapp/customerloandetails").hasAuthority("loan-app-adviser")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        final NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri)
                .build();
        OAuth2TokenValidator<Jwt> audienceValidator = new CustomValidator();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withCustomValidator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withCustomValidator);
        return jwtDecoder;
    }

    Converter<Jwt, ? extends AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new GrantedAuthoritiesExtractor());
        return jwtAuthenticationConverter;
    }

    static class GrantedAuthoritiesExtractor
            implements Converter<Jwt, Collection<GrantedAuthority>> {

        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<?> authorities = (Collection<?>)
                    jwt.getClaims().getOrDefault("cognito:groups", Collections.emptyList());

            return authorities.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

    }

    private Converter<Jwt, Collection<GrantedAuthority>> customJwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(CLAIM_ROLES);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
        return jwtGrantedAuthoritiesConverter;
    }

}
