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

    @GetMapping("/checkPatientDetails")
    public ResponseEntity<HashMap<String,Object>> checkPatientDetails(@RequestParam(name = "token") String token){
        HashMap<String,Object> resp=new HashMap<>();
        
        TokenClass tkn = new TokenClass(secretKey);
        if(!tkn.verifyToken(token)){
            resp.put("msg","Invalid Token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        int UID=Integer.parseInt(tkn.getPayload());
    
        Optional<Users> user=userService.getOneUsers(UID);
        if(!user.isPresent()){
            resp.put("msg","User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        int PID = user.get().getPatient_details().getPatient_id();
        Optional<Patient_details> pd = patientService.getOnePatient_details(PID);

        if(!pd.isPresent()){
            resp.put("msg", "Patient details not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        if (pd.get().getAddress()==null) {
            resp.put("msg","Patient Details form is not filled.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        resp.put("msg", "Patient details found");
        resp.put("patientDetails", pd.get());
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/setPatientDetails")
    public ResponseEntity<HashMap<String,Object>> setPatientDetails(@RequestBody HashMap<String, Object> req){
        HashMap<String,Object> resp = new HashMap<>();
        
        String token = (String) req.get("token");
        if (token == null || token.isEmpty()) {
            resp.put("msg", "Token is missing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        TokenClass tkn = new TokenClass(secretKey);
        if(!tkn.verifyToken(token)){
            resp.put("msg","Invalid Token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        int UID = Integer.parseInt(tkn.getPayload());

        Optional<Users> userOptional = userService.getOneUsers(UID);
        if (!userOptional.isPresent()) {
            resp.put("msg", "User with ID "+UID+" not found." );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }
        
        int PID = userOptional.get().getPatient_details().getPatient_id();

        // Retrieve patient details by PID
        Optional<Patient_details> patientOptional = patientService.getOnePatient_details(PID);
        if (!patientOptional.isPresent()) {
            resp.put("msg", "Patient with ID "+PID+" not found." );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
        }

        // Update patient details
        Patient_details patient = patientOptional.get();
        patient.setAddress((String) req.get("Address"));
        patient.setAllergies((String) req.get("Allergies"));
        try {
            SimpleDateFormat sdfl = new SimpleDateFormat("dd-MM-yyyy");

            // Parsing Date of Birth
            java.util.Date dobDate = sdfl.parse((String) req.get("DOB"));
            Date dobSqlDate = new Date(dobDate.getTime());
            patient.setDate_of_birth(dobSqlDate);

            patient.setEmail((String) req.get("Email"));
            String name = (String) req.get("Name");
            if (name != null && !name.isEmpty()) {
                String[] nameParts = name.split(" ");
                if (nameParts.length >= 2) {
                    patient.setFirst_name(nameParts[0]);
                    patient.setLast_name(nameParts[1]);
                }
            }
            patient.setGender((String) req.get("Gender"));
            patient.setPhone_number((String) req.get("PhoneNo"));
            patient.setMedical_conditions((String) req.get("MedicalCond"));
            patient.setMedications((String) req.get("Medication"));

            // Parsing Last Appointment Date
            java.util.Date lastAppDate = sdfl.parse((String) req.get("LastAppDate"));
            Date lastAppSqlDate = new Date(lastAppDate.getTime());
            patient.setLast_appointment_date(lastAppSqlDate);

            patient.setEmer_Name((String) req.get("EmerName"));
            patient.setEmer_Phn((String) req.get("EmerPhn"));
            patient.setEmer_Rel((String) req.get("EmerRel"));

            System.out.println(req.get("EmerName").toString()+req.get("EmerPhn").toString()+req.get("EmerRel").toString());
            
            // Save the updated patient details
            patientService.updatePatient_details(patient);
            
            resp.put("msg", "Patient details updated successfully");
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("msg", "Failed to update patient details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}   