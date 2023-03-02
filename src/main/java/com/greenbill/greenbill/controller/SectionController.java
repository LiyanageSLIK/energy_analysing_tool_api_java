package com.greenbill.greenbill.controller;

import com.greenbill.greenbill.dto.AddSectionDto;
import com.greenbill.greenbill.dto.CommonNodReqDto;
import com.greenbill.greenbill.service.PlayGroundService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addSection(@RequestBody CommonNodReqDto commonNodReqDto) {
        try {
            playGroundService.addNod(commonNodReqDto);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }


}
