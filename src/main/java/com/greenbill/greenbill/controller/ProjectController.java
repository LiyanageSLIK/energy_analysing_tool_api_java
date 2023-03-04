package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.refactor.AddProjectDto;
import com.greenbill.greenbill.dto.refactor.ResponseWrapper;
import com.greenbill.greenbill.dto.refactor.ProjectDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/test/project")
public class ProjectController {

    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> addProject(@RequestBody ProjectDto projectDto, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(playGroundService.addProject(addProjectReqResDto, userEmail), HttpStatus.OK.value(), "Success: Successfully added"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

//    @PostMapping("/create")
//    public ResponseEntity<ResponseWrapper> addProject(@RequestBody AddProjectDto addProjectReqResDto, @RequestHeader(value = "Authorization", required = true) String token) {
//        try {
//            String extractedToken = token.substring(7);
//            String userEmail = jwtUtil.extractEmail(extractedToken);
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(playGroundService.addProject(addProjectReqResDto, userEmail), HttpStatus.OK.value(), "Success: Successfully added"));
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(new ResponseWrapper(null, e.getStatusCode().value(), e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
//        }
//    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper> getAllProject(@RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(playGroundService.getAllProject(userEmail), HttpStatus.OK.value(), "Success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody AddProjectDto addProjectDto) {
        try {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(playGroundService.updateProject(addProjectDto), HttpStatus.OK.value(), "Success: Successfully updated"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @DeleteMapping("")
    public ResponseEntity deleteProject(@RequestParam long projectId) {
        try {
            playGroundService.deleteProject(projectId);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }


}
