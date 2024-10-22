package com.weather_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/sign-in")
    public String signIn() {
return "sign-in";
    }
}
