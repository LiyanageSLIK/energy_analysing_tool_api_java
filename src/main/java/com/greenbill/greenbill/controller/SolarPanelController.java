package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.response.SolarPanelDto;
import com.greenbill.greenbill.service.SolarPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/solar_panel")
public class SolarPanelController {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";

    @Autowired
    private SolarPanelService solarPanelService;

    @GetMapping("/")
    public ResponseEntity<ResponseWrapper> getAllProject(@RequestParam long projectId) {
        try {
            List<SolarPanelDto> allProjects = solarPanelService.getAllSolarPanelsForProject(projectId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(allProjects, HttpStatus.OK.value(), "Success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper> addSolarPanel(@RequestBody SolarPanelDto panelDto) {
        try {
            solarPanelService.addSolarPanel(panelDto);
            var successResponse = new ResponseWrapper(null, HttpStatus.OK.value(), "Success: Successfully added");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @DeleteMapping("/")
    public ResponseEntity deleteSolarPanel(@RequestParam long panelId) {
        try {
            solarPanelService.deletePanel(panelId);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }
}
