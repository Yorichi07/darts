package com.darts.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/test")
    public String test(){
        return "test case";
    }

    @GetMapping("/formFill")
    @ResponseBody
    public String getPatientDetails(@RequestParam(name = "token",required = false) String token){
        return token;
    }

}
