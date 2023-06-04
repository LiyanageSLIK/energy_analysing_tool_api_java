package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.response.ProjectSummaryDto;
import com.greenbill.greenbill.service.ReportGeneratorService;
import com.greenbill.greenbill.util.JwtUtil;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
    @Autowired
    private ReportGeneratorService reportGeneratorService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> generatePdfReport(@RequestHeader(value = "Authorization", required = true) String token,@RequestParam long projectId) throws DocumentException, IOException {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            ByteArrayOutputStream outputStream = reportGeneratorService.generateReport(projectId,userEmail);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "myreport.pdf");

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }
}
