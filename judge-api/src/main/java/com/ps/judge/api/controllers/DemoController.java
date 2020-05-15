package com.ps.judge.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/ok.html")
    public String ok(){
        return "ok";
    }
}
