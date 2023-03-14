package com.greenbill.greenbill.controller;


import com.greenbill.greenbill.dto.NodeDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.request.NodRequestDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/test/nod")
public class NodController {

    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/add")
    public ResponseEntity addNod(@RequestBody NodRequestDto nodRequestDto, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            playGroundService.addNode(nodRequestDto, userEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully added");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateNod(@RequestBody NodRequestDto nodRequestDto) {
        try {
            playGroundService.updateNode(nodRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity updateNod(@RequestParam String frontEndNodId) {
        try {
            playGroundService.deleteNod(frontEndNodId);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper(null, 500, "Internal Server Error"));
        }
    }


}
