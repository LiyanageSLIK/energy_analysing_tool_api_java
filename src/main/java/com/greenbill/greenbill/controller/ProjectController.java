package com.greenbill.greenbill.controller;


import com.greenbill.greenbill.dto.ProjectDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.service.ProjectService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/")
    public ResponseEntity<ResponseWrapper> addProject(@RequestBody ProjectDto projectDto,
                                                      @RequestHeader(value = "Authorization") String token) {
        try {
            var extractedToken = token.substring(7);
            var userEmail = jwtUtil.extractEmail(extractedToken);
            var addProjectResponseDto = projectService.addProject(projectDto, userEmail);
            var successResponse = new ResponseWrapper(addProjectResponseDto, HttpStatus.OK.value(), "Success: Successfully added");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper> getAllProject(@RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(projectService.getAllProject(userEmail), HttpStatus.OK.value(), "Success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @GetMapping("/")
    public ResponseEntity getProject(@RequestParam long projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(projectService.getProject(projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDto projectDto) {
        try {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(projectService.updateProject(projectDto), HttpStatus.OK.value(), "Success: Successfully updated"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @DeleteMapping("/")
    public ResponseEntity deleteProject(@RequestParam long projectId) {
        try {
            projectService.deleteProject(projectId);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }


}
