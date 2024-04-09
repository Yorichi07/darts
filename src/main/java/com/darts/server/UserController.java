package com.darts.server;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.darts.server.functions.CreateQr;
import com.darts.server.functions.PasswordHash;
import com.darts.server.functions.TokenClass;
import com.darts.server.model.Patient_details;
import com.darts.server.model.Users;
import com.darts.server.service.PatientsService;
import com.darts.server.service.UserService;

import ch.qos.logback.core.subst.Token;

@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("/api")
public class UserController {

    //user Service
    @Autowired
    UserService userService;

    //Patient Service
    @Autowired
    PatientsService patientService;

    //private key
    @Value("${secrets.secretkey}")
    private String secretKey;

    @GetMapping("/test")
    public boolean test(){
        TokenClass tkn = new TokenClass(secretKey);
        return tkn.verifyToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzEyNjAyODcyLCJleHAiOjE3MTI2MDY0NzJ9.QTeIpOKhzwP6ddWiBNPVkYW6b5xD4zsXaJihu5XY5uoHREIE467-UagGjzDsyyJoTeaoi2nPZIVsW4F-V-ZFQW");
    }

    // Add Users
    @PostMapping("/addUsers")
    public ResponseEntity<HashMap<String,Object>> addUser(@RequestBody HashMap<String,String> req){

        //Hashing function
        PasswordHash psswrdHash = new PasswordHash();

        HashMap<String,Object> resp = new HashMap<>();

        if(userService.getUserFromUserName(req.get("UserName")).isPresent()){
            resp.put("msg", "User Already Exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
        }

        // set user object
        Users newUser = new Users();
        newUser.setName(req.get("Name"));
        newUser.setUsername(req.get("UserName"));
        Patient_details newPatient = new Patient_details();
        try{
            newPatient = patientService.createPatients(newPatient);
            newUser.setPatient_details(newPatient);
            String psswrd = psswrdHash.getHash(req.get("PassWord"));
            if(psswrd.equals("500")){
                resp.put("msg", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
            }
            newUser.setPassword(psswrd);

            newUser.setQrPath(CreateQr.generateAndSaveQRCode(psswrd, psswrd));
        }catch(Exception err){
            resp.put("msg", err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

        resp.put("msg", "User Not Created");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

    //Login
    @PostMapping("/userLogin")
    public ResponseEntity<HashMap<String,Object>> userLogin(@RequestBody HashMap<String,String> req){
        
        //Hashing function
        PasswordHash psswrdHash = new PasswordHash();
        //response
        HashMap<String,Object> resp = new HashMap<>();

        Optional<Users> user = userService.getUserFromUserName(req.get("UserName"));
        if(!user.isPresent()){
            resp.put("msg", "User Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        if(user.get().getPassword().equals((psswrdHash.getHash(req.get("PassWord"))))){
            resp.put("msg", "Login Sucessfull");
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }
        
        resp.put("msg", "Incorrect Password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }

    @GetMapping("/formFill")
    @ResponseBody
    public String getPatientDetails(@RequestParam(name = "token",required = false) String token, Model model){
        if(token==null){
            return "error";
        }

        //patient data retrieval logic by lakSHIT
        Optional<Patient_details> optionalPatient = patientService.getOnePatient(token);

        if (!optionalPatient.isPresent()) {
            return "error";
        }

        Patient_details patient = optionalPatient.get();

        
        model.addAttribute("firstname", patient.getFirst_name());
        model.addAttribute("lastname", patient.getLast_name());
        model.addAttribute("dateofbirth", patient.getDate_of_birth());
        model.addAttribute("gender", patient.getGender());
        model.addAttribute("medical_conditions", patient.getMedical_conditions());
        model.addAttribute("medications", patient.getMedications());
        model.addAttribute("allergies", patient.getAllergies());
        model.addAttribute("last_appointment_date", patient.getLast_appointment_date());
        model.addAttribute("phone_number", patient.getPhone_number());
        model.addAttribute("email", patient.getEmail());
        model.addAttribute("address", patient.getAddress());

        return "patientform";
    }

}
