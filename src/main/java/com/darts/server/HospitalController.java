package com.darts.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.darts.server.functions.DecisionTreeSymp;
import com.darts.server.functions.HospitalRecords;
import com.darts.server.functions.PasswordHash;
import com.darts.server.functions.TokenClass;
import com.darts.server.model.Patient_details;
import com.darts.server.model.Specialist;
import com.darts.server.model.Users;
import com.darts.server.service.Patient_detailsService;
import com.darts.server.service.SpecialistService;
import com.darts.server.service.UserService;

@CrossOrigin(origins = "*")
@PropertySource("classpath:application.properties")
@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    SpecialistService specialistService;

    @Autowired
    UserService userService;

    @Autowired
    Patient_detailsService patient_detailsService;
    
    @Value("${secrets.secretkey}")
    private String secretKey;

     //Secret Key Doctor
    @Value("${secrets.secretkeydoc}")
    private String docSecretKey;


    @PostMapping("/doctorPunchIn")
    public ResponseEntity<HashMap<String,String>> doctorpunchin(@RequestBody HashMap<String, String>req){
            //Token Class Doctor
            TokenClass docTkn = new TokenClass(docSecretKey);

            HashMap<String,String> resp = new HashMap<>();
            if(docTkn.verifyToken(req.get("hospitaltkn"))){
                if(docTkn.verifyToken(req.get("doctortkn"))){
                    Integer DID = Integer.parseInt(docTkn.getPayload());
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
        TokenClass tkn = new TokenClass(docSecretKey);
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
            return ResponseEntity.ok(resp);
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
            HospitalRecords.insertPat(UID, speciality,specialistService);
            resp.put("msg", "Doctor Assigned");
            return ResponseEntity.ok(resp);
        }
        resp.put("msg", "Invalid Token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/getSymptoms")
    public ResponseEntity<List<Object>> getSymptomps(){
        return ResponseEntity.ok(DecisionTreeSymp.jsonArray);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/assignDocBySymp")
    public ResponseEntity<HashMap<String,Object>> assignDocBySymp(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,@RequestBody HashMap<String,Object> req){
        HashMap<String,Object> resp = new HashMap<>();
        TokenClass tkn = new TokenClass(secretKey);

        if(tkn.verifyToken(token.split(" ")[1])){
            Integer UID = Integer.parseInt(tkn.getPayload());
            if(HospitalRecords.insertPat(UID, DecisionTreeSymp.getDisease((ArrayList<String>) req.get("list")), specialistService)){
                resp.put("msg","doctor assigned");
            }
            resp.put("msg","no doctor available");
            return ResponseEntity.ok(resp);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/getQrPath")
    public ResponseEntity<HashMap<String,Object>> getQrPath(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        //Token Class Doctor
        TokenClass docTkn = new TokenClass(docSecretKey);
        HashMap<String,Object> resp = new HashMap<>();
        if(docTkn.verifyToken(token.split(" ")[1])){
            resp.put("path", specialistService.getOneSpecialist(Integer.parseInt(docTkn.getPayload())).get().getQrPath());
            return ResponseEntity.ok(resp);
        }
        resp.put("msg", "Invalid Token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    //Login
    @PostMapping("/docLogin")
    public ResponseEntity<HashMap<String,Object>> userLogin(@RequestBody HashMap<String,String> req){
        
        //Hashing function
        PasswordHash psswrdHash = new PasswordHash();
        //response
        HashMap<String,Object> resp = new HashMap<>();
        //Token class
        TokenClass tkn = new TokenClass(docSecretKey);

        Optional<Specialist> user = specialistService.getDocFromUserName(req.get("UserName"));
        
        if(!user.isPresent()){
            resp.put("msg", "Doc Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        if(user.get().getPassword().equals((psswrdHash.getHash(req.get("PassWord"))))){
            resp.put("token", tkn.generateToken(user.get().getDocID(), true));
            resp.put("msg", "Login Sucessfull");
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }
        
        resp.put("msg", "Incorrect Password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/getNextPat")
    public ResponseEntity<HashMap<String,Object>> getNextPat(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        TokenClass tokenClass = new TokenClass(docSecretKey);
        HashMap<String,Object> resp = new HashMap<>();

        if(tokenClass.verifyToken(token.split(" ")[1])){
            int DID = Integer.parseInt(tokenClass.getPayload());
            int UID = HospitalRecords.getPat(DID, specialistService);
            if(UID == -1){
                resp.put("msg", "No patient Assigned");
                return ResponseEntity.ok(resp);
            }
            Users users = userService.getOneUsers(UID).get();
            Patient_details pat = patient_detailsService.getOnePatient_details(users.getPatient_details().getPatient_id()).get();
            pat.setUsers(null);
            resp.put("msg", "patient fetched");
            resp.put("patient", pat);
            return ResponseEntity.ok(resp);
        }
        resp.put("msg", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }
}





