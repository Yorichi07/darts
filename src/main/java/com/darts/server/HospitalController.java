package com.darts.server;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.darts.server.functions.HospitalRecords;
import com.darts.server.functions.TokenClass;
import com.darts.server.model.Specialist;
import com.darts.server.service.SpecialistService;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    SpecialistService specialistService;
    
    @Value("${secrets.secretkey}")
    private String secretKey;

    @PostMapping("/doctorPunchIn")
    public ResponseEntity<HashMap<String,String>> doctorpunchin(@RequestBody HashMap<String, String>req){
            TokenClass tkn = new TokenClass(secretKey);
            HashMap<String,String> resp = new HashMap<>();
            if(tkn.verifyToken(req.get("hospitaltkn"))){
                if(tkn.verifyToken(req.get("doctortkn"))){
                    Integer DID = Integer.parseInt(tkn.getPayload());
                    Specialist sp = specialistService.getOneSpecialist(DID).get();
                    HospitalRecords.insertDoc(sp);
                    resp.put("msg", "Doctor Punched In");
                    return ResponseEntity.status(HttpStatus.OK).body(resp);
                }
            }
        resp.put("msg", "Doctor Not Valid");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);

    }


}



