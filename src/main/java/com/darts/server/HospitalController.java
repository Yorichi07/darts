package com.darts.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@RequestMapping("/api/hospital")
public class HospitalController implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        String path = "file:///C:/college/minor2/darts/src/main/resources/static";
        registry.addResourceHandler("/api/hospital/getPatientDetails")
                .addResourceLocations(path);
    }

    @GetMapping("/getPatientDetails")
    public String getPatientDetails(Model model) {

            
            // Patient_details patient = new Patient_details();

            model.addAttribute("firstname", "LAKSHIT");
            // model.addAttribute("lastname", "");
            // model.addAttribute("dateofbirth", "");
            // model.addAttribute("gender", "");
            // model.addAttribute("medical_conditions", "");
            // model.addAttribute("medications", "");
            // model.addAttribute("allergies", "");
            // model.addAttribute("last_appointment_date", "");
            // model.addAttribute("phone_number", "");
            // model.addAttribute("email", "");
            // model.addAttribute("address", "");

            // Since we're redirecting, there's no need to return any string here
            return "patientform";
    }
}

