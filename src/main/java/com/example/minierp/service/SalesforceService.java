package com.example.minierp.service;

import com.example.minierp.Customer;
import com.example.minierp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SalesforceService {

    @Value("${salesforce.client-id}")
    private String clientId;

    @Value("${salesforce.client-secret}")
    private String clientSecret;

    @Value("${salesforce.token-url}")
    private String tokenUrl;

    private final RestClient restClient = RestClient.create();

    private final CustomerRepository customerRepository;

    public SalesforceService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public SalesforceTokenResponse getAccessToken() {

        return restClient.post()
                .uri(tokenUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials"
                        + "&client_id=" + clientId
                        + "&client_secret=" + clientSecret)
                .retrieve()
                .body(SalesforceTokenResponse.class);
    }

    public SalesforceTokenResponse testConnection() {
        return getAccessToken();
    }

    public String createAccount(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        SalesforceTokenResponse token = getAccessToken();

        String jsonBody = """
            {
                "Name": "%s",
                "Phone": "%s",
                "Tax_ID__c": "%s"
            }
            """.formatted(customer.getName(), customer.getPhone(), customer.getTaxIdNo());

        return restClient.post()
                .uri(token.getInstance_url() + "/services/data/v67.0/sobjects/Account/")
                .header("Authorization", "Bearer " + token.getAccess_token())
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .retrieve()
                .body(String.class);
    }
}