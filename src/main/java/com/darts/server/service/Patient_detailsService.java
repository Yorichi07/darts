package com.darts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darts.server.model.Patient_details;
import com.darts.server.repository.Patient_detailsRepository;

@Service
public class Patient_detailsService {
    @Autowired
    Patient_detailsRepository patRepo;

    //Create
    public Patient_details createPatient_details(Patient_details pat){
        return patRepo.save(pat);
    }

    //Read All
    public List<Patient_details> getAllPatient_details(){
        return (List<Patient_details>) patRepo.findAll();
    }

    //Find One
    public Optional<Patient_details> getOnePatient_details(Integer id){
        return patRepo.findById(id);
    }

    //Update 
}
