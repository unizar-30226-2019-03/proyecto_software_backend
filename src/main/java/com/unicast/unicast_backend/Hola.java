package com.unicast.unicast_backend;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Hola {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Adri!";
    }
    
}

