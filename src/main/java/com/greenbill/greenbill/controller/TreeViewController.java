package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.TreeViewReqResDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/test/treeView")
public class TreeViewController {

    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("/update")
    public ResponseEntity updateTreeViewJson(@RequestBody TreeViewReqResDto treeViewReqResDto, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            return ResponseEntity.status(HttpStatus.OK).body(playGroundService.updateTree(treeViewReqResDto,userEmail).getJson());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

}
