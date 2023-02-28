package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.dto.ResponseWrapper;
import com.greenbill.greenbill.dto.UserRegisterDto;
import com.greenbill.greenbill.service.PlayGroundService;
import com.greenbill.greenbill.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RestController
@RequestMapping("/test/section")
public class SectionController {

    @Autowired
    private PlayGroundService playGroundService;

    @PostMapping("/add")
    public void addSection(@RequestBody AddSectionDto addSectionDto) {
        try {
            playGroundService.addSection(addSectionDto);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }


}
