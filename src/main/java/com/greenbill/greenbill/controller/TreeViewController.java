package com.greenbill.greenbill.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.dto.refactor.ResponseWrapper;
import com.greenbill.greenbill.dto.TreeViewReqResDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@Controller
//@RestController
//@RequestMapping("/test/treeView")
@Deprecated
public class TreeViewController {

    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("")
    public ResponseEntity updateTreeViewJson(@RequestBody JsonNode jsonNode,@RequestParam long projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(playGroundService.updateTree(jsonNode,projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @GetMapping("")
    public ResponseEntity getTreeViewJson(@RequestParam long projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(playGroundService.getTree(projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

}
