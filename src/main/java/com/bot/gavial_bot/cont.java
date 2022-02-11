package com.bot.gavial_bot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class cont {

    @GetMapping("/hello")
    public String hello(){
        return "hello heruko";
    }

}
