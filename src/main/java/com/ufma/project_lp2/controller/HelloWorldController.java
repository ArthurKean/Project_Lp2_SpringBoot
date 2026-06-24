package com.ufma.project_lp2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //C
@RequestMapping("/Hello-world")
public class HelloWorldController {

    @GetMapping
    public String helloWorld(){
        return "Hello world";
    }
}
