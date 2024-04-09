package com.darts.server.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darts.server.model.Patient_details;
import com.darts.server.repository.PatientRepository;

@Service
@SuppressWarnings("null")
public class PatientService {
    @Autowired
    PatientRepository patRepo;

    //create
    
    public Patient_details createPatient_details(Patient_details ptd){
        return patRepo.save(ptd);
    }

    public Optional<Patient_details> getOnePatient_details(Integer PID){
        return patRepo.findById(PID);
    }

}
