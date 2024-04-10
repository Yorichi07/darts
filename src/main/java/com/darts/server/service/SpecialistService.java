package com.darts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darts.server.model.Specialist;

import com.darts.server.repository.SpecialistRepository;

@Service
public class SpecialistService {
    @Autowired
    SpecialistRepository speRepo;

    //Create
    public Specialist createSpecialist(Specialist spe){
        return speRepo.save(spe);
    }

    //Read All
    public List<Specialist> getAllSpecialist(){
        return (List<Specialist>) speRepo.findAll();
    }

    //Find one
    public Optional<Specialist> getOneSpecialist(Integer id){
        return speRepo.findById(id);
    }

    //Find by doc_name
    public Optional<Specialist> getSpecialistFromName(String docName){
        return speRepo.findByDocName(docName);
    }

    //Find by speciality
    public Optional<Specialist> getSpecialistFromSpeciality(String Speciality){
        return speRepo.findBySpeciality(Speciality);
    }

    //Set emergency status to yes
    public Specialist setEmergencyToYes(Integer id){
        Specialist spec=speRepo.findById(id).get();
        spec.setEmergency("YES");
        return speRepo.save(spec);
    }

    //Set emergency status to no
    public Specialist setEmergencyToNo(Integer id){
        Specialist spec=speRepo.findById(id).get();
        spec.setEmergency("NO");
        return speRepo.save(spec);
    }
}
