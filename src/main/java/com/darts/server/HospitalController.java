package com.darts.server;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.darts.server.model.Specialist;

@Controller
@RequestMapping("/api/hospital")
public class HospitalController {

    public static List<Specialist> availableDoctors;

    

}



