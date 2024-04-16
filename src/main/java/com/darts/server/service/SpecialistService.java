package com.darts.server.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    //get distinct specialities
    public Set<String> getDistinctSpecialities(){
        return speRepo.findDistinctBySpeciality();
    }

    //get doctors of a particualar speciality
    public List<Specialist> getDoctorsBySpeciality(String speciality){
        return speRepo.findAllBySpeciality(speciality);
    }

    //update qr path
    public Specialist updateSpecialistQrPath(Specialist doc,String qrPath){
        Specialist newDoc = speRepo.findById(doc.getDocID()).get();
        newDoc.setQrPath(qrPath);
        return speRepo.save(newDoc);
    }

    //find by username
    public Optional<Specialist> getDocFromUserName(String userName){
        return speRepo.findByUsername(userName);
    }
}
