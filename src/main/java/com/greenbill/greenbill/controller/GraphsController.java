package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.service.PlayGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RestController
@RequestMapping("/graphs")
public class GraphsController {
    @Autowired
    private PlayGroundService playGroundService;

    @GetMapping("/section")
    public ResponseEntity getSectionGraphsDetails(@RequestParam String frontEndId) {
        try {
            var resultDto = playGroundService.getSectionGraphsDetails(frontEndId);
            var successResponse = new ResponseWrapper(resultDto, HttpStatus.OK.value(), "Success: Successfully generated");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @GetMapping("/project")
    public ResponseEntity getSectionGraphsDetails(@RequestParam long projectId) {
        try {
            var resultDtoList = playGroundService.getProjectGraphsDetails(projectId);
            var successResponse = new ResponseWrapper(resultDtoList, HttpStatus.OK.value(), "Success: Successfully generated");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

}
