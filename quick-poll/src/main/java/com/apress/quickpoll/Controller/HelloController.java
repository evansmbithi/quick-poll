package com.apress.quickpoll.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping(value="hello")
    public String getGreeting(){
        return "Hello Spring";
    }
}
