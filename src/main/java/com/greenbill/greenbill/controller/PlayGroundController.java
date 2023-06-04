package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.service.PlayGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RestController
@RequestMapping("/playground")
public class PlayGroundController {
    private static final String SUCCESSFULLY_GENERATE_MESSAGE = "Success: Successfully generated";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error.";

    @Autowired
    private PlayGroundService playGroundService;

    @GetMapping("graphs/section")
    public ResponseEntity getSectionGraphsDetails(@RequestParam String frontEndId) {
        try {
            var resultDto = playGroundService.getSectionGraphsDetails(frontEndId);
            var successResponse = new ResponseWrapper(resultDto, HttpStatus.OK.value(), SUCCESSFULLY_GENERATE_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @GetMapping("graphs/project")
    public ResponseEntity getProjectGraphsDetails(@RequestParam long projectId) {
        try {
            var resultDtoList = playGroundService.getProjectGraphsDetails(projectId);
            var successResponse = new ResponseWrapper(resultDtoList, HttpStatus.OK.value(), SUCCESSFULLY_GENERATE_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @GetMapping("bill")
    public ResponseEntity getCalculatedBill(@RequestParam long projectId) {
        try {
            var result = playGroundService.calculateBill(projectId);
            var successResponse = new ResponseWrapper(result, HttpStatus.OK.value(), SUCCESSFULLY_GENERATE_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @GetMapping("simpleBill")
    public ResponseEntity simpleBillCalculator(@RequestParam double units) {
        try {
            var resultDtoList = playGroundService.simpleBillCalculator(units);
            var successResponse = new ResponseWrapper(resultDtoList, HttpStatus.OK.value(), SUCCESSFULLY_GENERATE_MESSAGE);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @GetMapping("section")
    public ResponseEntity getProject(@RequestParam String frontEndId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(playGroundService.getSectionSummary(frontEndId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

}
