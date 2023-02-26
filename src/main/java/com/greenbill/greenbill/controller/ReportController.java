package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.service.ReportGeneratorService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportGeneratorService reportGeneratorService;

    @GetMapping("/pdf")
    public ResponseEntity<ByteArrayResource> generatePdfReport() throws DocumentException, IOException {
//        List<MyEntity> entities = // get your entities from a database or other source
        ByteArrayOutputStream outputStream = reportGeneratorService.generateReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "myreport.pdf");

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
