package com.darts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.darts.server.model.Patient_details;
import com.darts.server.repository.PatientsRepository;

public class PatientsService {
    @Autowired
    PatientsRepository patRepo;

    //Create
    public Patient_details createPatients(Patient_details pat){
        return patRepo.save(pat);
    }

    //Read All
    public List<Patient_details> getAllPatients(){
        return (List<Patient_details>) patRepo.findAll();
    }

    //Find One
    public Optional<Patient_details> getOnePatient(Integer id){
        return patRepo.findById(id);
    }
}
