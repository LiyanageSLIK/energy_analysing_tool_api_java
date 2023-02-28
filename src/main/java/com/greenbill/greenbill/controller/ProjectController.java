package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.AddProjectReqResDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.service.PlayGroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/test/project")
public class ProjectController {

    @Autowired
    private PlayGroundService playGroundService;

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> addProject(@RequestBody AddProjectReqResDto addProjectReqResDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper(playGroundService.addProject(addProjectReqResDto), HttpStatus.OK.value(), "Success: Successfully added"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseWrapper(null, 400, "Bad Request"));
        }
    }
}
