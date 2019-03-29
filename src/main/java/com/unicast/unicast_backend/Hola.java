package com.unicast.unicast_backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hola {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Adri!";
    }
    
}

