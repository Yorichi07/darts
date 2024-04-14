package com.darts.server;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.darts.server.functions.CreateQr;
import com.darts.server.functions.DocAllocation;
import com.darts.server.functions.NearestHospital;
import com.darts.server.functions.PasswordHash;
import com.darts.server.functions.TokenClass;
import com.darts.server.model.Hospital;
import com.darts.server.model.Patient_details;
import com.darts.server.model.Specialist;
import com.darts.server.model.Users;
import com.darts.server.service.HospitalService;
import com.darts.server.service.Patient_detailsService;
import com.darts.server.service.SpecialistService;
import com.darts.server.service.UserService;

@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("/api/user")
public class UserController {

    //user Service
    @Autowired
    UserService userService;

    //Patient Service
    @Autowired
    Patient_detailsService patientService;

    //Specialist Service
    @Autowired
    SpecialistService specialistService;

    //Hospital Service
    @Autowired
    HospitalService hosSer;

    //private key
    @Value("${secrets.secretkey}")
    private String secretKey;

    @GetMapping("/test")
    public String[] test(){
        TokenClass tkn = new TokenClass(secretKey);
        String[] resp = {tkn.generateToken(498, false),tkn.generateToken(112, false),tkn.generateToken(111, false)};
        return resp;
    }

    // Add Users
    @PostMapping("/addUsers")
    public ResponseEntity<HashMap<String,Object>> addUser(@RequestBody HashMap<String,String> req){

        //Hashing function
        PasswordHash psswrdHash = new PasswordHash();
        //Response
        HashMap<String,Object> resp = new HashMap<>();
        // Token Class
        TokenClass tokenClass = new TokenClass(secretKey);

        if(userService.getUserFromUserName(req.get("UserName")).isPresent()){
            resp.put("msg", "User Already Exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
        }

        // set user object
        Users newUser = new Users();
        newUser.setName(req.get("Name"));
        newUser.setUsername(req.get("UserName"));
        
        try{
            // create patient
            Patient_details newPatient = new Patient_details();
            newPatient.setAddress(req.get("Address"));
            newPatient.setAllergies(req.get("Allergies"));
            // get date sql format
            SimpleDateFormat sdfl = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date date = sdfl.parse(req.get("DOB"));
            Date sqlDate = new Date(date.getTime());

            newPatient.setDate_of_birth(sqlDate);
            newPatient.setEmail(req.get("Email"));
            newPatient.setFirst_name(req.get("Name").split(" ")[0]);
            newPatient.setLast_name(req.get("Name").split(" ")[1]);
            newPatient.setGender(req.get("Gender"));
            newPatient.setPhone_number(req.get("PhoneNo"));
            newPatient.setMedical_conditions(req.get("MedicalCond"));
            newPatient.setMedications(req.get("Medication"));
            
            date = sdfl.parse(req.get("LastAppDate"));
            sqlDate = new Date(date.getTime());
            newPatient.setLast_appointment_date(sqlDate);

            newPatient.setEmer_Name(req.get("EmerName"));
            newPatient.setEmer_Phn(req.get("EmerPhn"));
            newPatient.setEmer_Rel(req.get("EmerRel"));

            newPatient = patientService.createPatient_details(newPatient);
            //set patient to user
            newUser.setPatient_details(newPatient);
            String psswrd = psswrdHash.getHash(req.get("PassWord"));
            if(psswrd.equals("500")){
                resp.put("msg", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
            }
            newUser.setPassword(psswrd);

            newUser = userService.createUsers(newUser);
            String tkn = tokenClass.generateToken(newUser.getUID(),false);
            
            String qrPath = CreateQr.generateAndSaveQRCode(tkn, newUser.getUID().toString());

            userService.updateUsersQrPath(qrPath, newUser);

            resp.put("token", tkn);
            resp.put("msg", "User Created");
            resp.put("qrPath", "http://localhost:8080/"+qrPath);
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        }catch(Exception err){
            resp.put("msg", err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @GetMapping("/isPatient")
    public ResponseEntity<HashMap<String,Object>> isPatient(@RequestParam(name = "token") String token) {
        
        HashMap<String,Object> resp = new HashMap<>();
        
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

    @PostMapping("/docAllocation")
    public ResponseEntity<HashMap<String,Object>> allocateDoctor(@RequestBody HashMap<String, Object> req){
        int urgency = (int) req.get("urgency");
        String searchTerm = null;

        if (req.containsKey("searchTerm")) {
            searchTerm = (String) req.get("searchTerm");
        }

        DocAllocation docAllocation = new DocAllocation();
        List<Specialist> specialists = specialistService.getAllSpecialist();
        Specialist allocatedDoctor = docAllocation.allocateDoc(specialists, urgency, searchTerm);

        HashMap<String, Object> resp = new HashMap<>();
        if (allocatedDoctor != null) {
            resp.put("msg", "Doctor allocated successfully");
            resp.put("doctor", allocatedDoctor);
            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } else {
            resp.put("msg", "No doctor available for allocation");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
    }

    @PostMapping("/nearestHospital")
    public ResponseEntity<HashMap<String,Object>> findNearestHospital(@RequestBody HashMap<String, Double> req){
        double curLat = req.get("curLat");
        double curLong = req.get("curLong");

        NearestHospital nearHos = new NearestHospital(hosSer);
        List<Hospital> hos = nearHos.getNearestHospital(curLat, curLong);

        HashMap<String,Object> resp = new HashMap<>();
        if (!hos.isEmpty()) {
            resp.put("msg", "Nearest hospitals found successfully");
            resp.put("hospitals", hos); 
            return ResponseEntity.ok(resp);
        } else {
            resp.put("msg", "No hospitals available nearby");
            return ((BodyBuilder) ResponseEntity.notFound()).body(resp); 
        }
    }

}
