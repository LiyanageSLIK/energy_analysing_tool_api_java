package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.request.SubscriptionDto;
import com.greenbill.greenbill.service.SubscriptionService;
import com.greenbill.greenbill.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("")
    public ResponseEntity<ResponseWrapper> addProject(@RequestBody @Valid SubscriptionDto subscriptionDto,
                                                      @RequestHeader(value = "Authorization") String token) {
        try {
            var extractedToken = token.substring(7);
            var userEmail = jwtUtil.extractEmail(extractedToken);
            subscriptionService.addSubscription(userEmail, subscriptionDto);
            var successResponse = new ResponseWrapper(null, HttpStatus.OK.value(), "Success: Successfully Subscribed");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }


}
