package com.darts.server;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.darts.server.functions.TokenClass;
import com.darts.server.model.Specialist;
import com.darts.server.service.SpecialistService;

@PropertySource("classpath:application.properties")
@Controller
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    SpecialistService specialistService;
    
    @Value("${secrets.secretkey}")
    private String secretKey;
    @GetMapping("/doctorpunchin")
    public Specialist doctorpunchin(@RequestBody HashMap<String, String>req){
            TokenClass tkn = new TokenClass(secretKey);

            if(tkn.verifyToken(req.get("hospitaltkn"))){
                if(tkn.verifyToken(req.get("doctortkn"))){
                    Integer DID = Integer.parseInt(tkn.getPayload());
                    Specialist sp = specialistService.getOneSpecialist(DID).get();
                    return sp;
                }
            }
        return null;

    }
}



