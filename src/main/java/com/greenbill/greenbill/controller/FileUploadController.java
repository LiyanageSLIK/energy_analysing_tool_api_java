package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.request.UserRegisterDto;
import com.greenbill.greenbill.service.UploadHandlerService;
import com.greenbill.greenbill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private UploadHandlerService uploadHandlerService;

    @PostMapping("/subscription_plan")
    public ResponseEntity<ResponseWrapper> uploadSubscriptionPlanCsv(@RequestParam("file") MultipartFile file) {
        try {
            uploadHandlerService.uploadSubscriptionPlan(file);
            var successResponse = new ResponseWrapper("Successfully Uploaded", HttpStatus.OK.value(), "Success: Successfully Uploaded");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        }
    }
}
