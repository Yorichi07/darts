package com.darts.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.darts.server.functions.TokenClass;
import com.darts.server.model.Patient_details;
import com.darts.server.model.Users;
import com.darts.server.service.Patient_detailsService;
import com.darts.server.service.SpecialistService;
import com.darts.server.service.UserService;


@CrossOrigin(origins = "*")
@Controller
@PropertySource("classpath:application.properties")
@RequestMapping("/api/pages")
public class WebMvcController implements WebMvcConfigurer{

    @Autowired
    UserService userService;

    @Autowired
    Patient_detailsService patient_detailsService;

    @Autowired
    SpecialistService specialistService;

    @Value("${secrets.secretkey}")
    private String secretKey;

    //Secret Key Doctor
    @Value("${secrets.secretkeydoc}")
    private String docSecretKey;

    @GetMapping("/docAuth")
    public String Signup(){
        return "doctorPortal/Sign";
    }

    @GetMapping("/docQrCode")
    public String docQrCode(){
        return "doctorPortal/qr";
    }

    @GetMapping("/scanQrPat")
    public String patQrCode(){
        return "qrPat";
    }

    @GetMapping("/scanQr")
    public String scanQr(){
        return "qrgen";
    }

    @GetMapping("/getPatient")
    public String getPatient(Model model){
        model.addAttribute("firstname", "Patient Not Assigned Yet");
        return "doctorPortal/doc";
    }

    @GetMapping("/patientDashboard")
    public String Patient_details_signin(){
        return "patientPortal/patientdash";
    }

    @GetMapping("/getPatientDetails")
    public String getPatientDetails(@RequestParam(name = "token",required = false) String token,Model model) {
        if(token == null){
            return "error";
        }
        TokenClass tkn = new TokenClass(secretKey);
        if(tkn.verifyToken(token)){
            Integer UID = Integer.parseInt(tkn.getPayload());
            Users usr = userService.getOneUsers(UID).get();
            Integer patient_Id = usr.getPatient_details().getPatient_id();

            Patient_details patient = patient_detailsService.getOnePatient_details(patient_Id).get();

            model.addAttribute("firstname", patient.getFirst_name());
            model.addAttribute("lastname",patient.getLast_name());
            model.addAttribute("dateofbirth", patient.getDate_of_birth());
            model.addAttribute("gender", patient.getGender());
            model.addAttribute("medical_conditions", patient.getMedical_conditions());
            model.addAttribute("medications", patient.getMedications());
            model.addAttribute("allergies", patient.getAllergies());
            model.addAttribute("last_appointment_date", patient.getLast_appointment_date());
            model.addAttribute("phone_number", patient.getPhone_number());
            model.addAttribute("email", patient.getEmail());
            model.addAttribute("address", patient.getAddress());
            model.addAttribute("emer_name", patient.getEmer_name());
            model.addAttribute("emer_rel", patient.getEmer_Rel());
            model.addAttribute("emer_phn", patient.getEmer_Phn());
            
            // Since we're redirecting, there's no need to return any string here
            return "patientRetrieval/patientR";
        }


        return "error";
    }
}
