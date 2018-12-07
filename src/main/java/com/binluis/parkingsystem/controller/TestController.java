package com.binluis.parkingsystem.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(produces = {"application/json"})
    public String list() {
        return "{test:success}";
    }


}