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
    public Patient_details updatePatient_details(Patient_details pat){
        Patient_details newPat = getOnePatient_details(pat.getPatient_id()).get();
        newPat.setAddress(pat.getAddress());
        newPat.setAllergies(pat.getAllergies());
        newPat.setDate_of_birth(pat.getDate_of_birth());
        newPat.setEmail(pat.getEmail());
        newPat.setFirst_name(pat.getFirst_name());
        newPat.setLast_name(pat.getLast_name());
        newPat.setGender(pat.getGender());
        newPat.setPhone_number(pat.getPhone_number());
        newPat.setMedical_conditions(pat.getMedical_conditions());
        newPat.setMedications(pat.getMedications());
        newPat.setLast_appointment_date(pat.getLast_appointment_date());
        newPat.setEmer_Name(pat.getEmer_name());
        newPat.setEmer_Phn(pat.getEmer_Phn());
        newPat.setEmer_Rel(pat.getEmer_Rel());
        return patRepo.save(newPat);
    }
}
