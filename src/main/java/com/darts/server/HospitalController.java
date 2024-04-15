package com.darts.server;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/doctorPunchOut")
    public ResponseEntity<HashMap<String,String>> doctorPunchOut(@RequestParam(name = "token") String token){
        TokenClass tkn = new TokenClass(secretKey);
        HashMap<String,String> resp = new HashMap<>();

        if(tkn.verifyToken(token)){
            Integer DID = Integer.parseInt(tkn.getPayload());
            Specialist doc = specialistService.getOneSpecialist(DID).get();
            HospitalRecords.deleteDoc(doc);
            resp.put("msg", "Doctor Punched Out");
            return ResponseEntity.ok(resp);
        }
        resp.put("msg", "Invalid Token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/getSpeciality")
    public Object[] getSpeciality(){
        return specialistService.getDistinctSpecialities().toArray();
    }

    @GetMapping("/getDocs")
    public Object[] getDocs(@RequestParam(name = "speciality") String speciality){
        return specialistService.getDoctorsBySpeciality(speciality).toArray();
    }

    @GetMapping("/assignDocByName")
    public ResponseEntity<HashMap<String,Object>> assignDocByName(@RequestParam(name = "did") String DID,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TokenClass tkn = new TokenClass(secretKey);
        HashMap<String,Object> resp = new HashMap<>();
        if(tkn.verifyToken(token.split(" ")[1])){
            Specialist doc = specialistService.getOneSpecialist(Integer.parseInt(DID)).get();
            HospitalRecords.insertPatDoc(Integer.parseInt(tkn.getPayload()), doc);
            resp.put("msg", "Doctor assigned");
        }
        resp.put("msg", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/assignDocBySpec")
    public ResponseEntity<HashMap<String,Object>> assignDocBySpec(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,@RequestParam(name = "Speciality") String speciality){
        TokenClass tkn = new TokenClass(secretKey);
        HashMap<String,Object> resp = new HashMap<>();

        if(tkn.verifyToken(token.split(" ")[1])){
            int UID = Integer.parseInt(tkn.getPayload());
            HospitalRecords.insertPat(UID, speciality);
            resp.put("msg", "Doctor Assigned");
            return ResponseEntity.ok(resp);
        }
        resp.put("msg", "Invalid Token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @PostMapping("/assignDocBySymp")
    public ResponseEntity<HashMap<String,Object>> assignDocBySymp(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,@RequestBody HashMap<String,Object> req){
        return null;
    }
}





