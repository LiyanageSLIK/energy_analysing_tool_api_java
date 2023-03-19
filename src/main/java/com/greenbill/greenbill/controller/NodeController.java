package com.greenbill.greenbill.controller;


import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.request.NodeRequestDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("/node")
public class NodeController {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error.";

    @Autowired
    private PlayGroundService playGroundService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/add")
    public ResponseEntity addNode(@RequestBody NodeRequestDto nodeRequestDto,
                                  @RequestHeader(value = "Authorization") String token) {
        try {
            String extractedToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(extractedToken);
            playGroundService.addNode(nodeRequestDto, userEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully added");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @PutMapping("/update")
    public ResponseEntity updateNod(@RequestBody NodeRequestDto nodeRequestDto) {
        try {
            playGroundService.updateNode(nodeRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }

    @DeleteMapping("")
    public ResponseEntity updateNod(@RequestParam String frontEndId) {
        try {
            playGroundService.deleteNode(frontEndId);
            return ResponseEntity.status(HttpStatus.OK).body("Success: Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper(null, 500, INTERNAL_SERVER_ERROR_MESSAGE));
        }
    }


}
