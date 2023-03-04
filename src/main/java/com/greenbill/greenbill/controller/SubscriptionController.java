package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.refactor.ResponseWrapper;
import com.greenbill.greenbill.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ResponseEntity<ResponseWrapper> getAllActivePlan() {
        try {
            var activeSubsPlanList = subscriptionService.getActiveSubsPlanList();
            var responseWrapper = new ResponseWrapper(activeSubsPlanList, HttpStatus.OK.value(), "Success: Successfully loaded");
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        } catch (HttpClientErrorException e) {
            var responseWrapper = new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(responseWrapper);
        }
    }

    //TODO: register free subscription plan should come here
}
