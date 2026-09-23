package com.example.minierp.controller;

import com.example.minierp.service.SalesforceService;
import com.example.minierp.service.SalesforceTokenResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/salesforce")
public class SalesforceController {

    private final SalesforceService salesforceService;

    public SalesforceController(SalesforceService salesforceService) {
        this.salesforceService = salesforceService;
    }

    @GetMapping("/test")
    public SalesforceTokenResponse testConnection() {
        return salesforceService.testConnection();
    }

    @PostMapping("/accounts/{customerId}")
    public String createAccount(@PathVariable Long customerId) {

        return salesforceService.createAccount(customerId);
    }
}