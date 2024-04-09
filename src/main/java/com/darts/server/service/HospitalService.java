package com.darts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darts.server.model.Hospital;
import com.darts.server.repository.HospitalRepository;

@Service
public class HospitalService {
    @Autowired
    HospitalRepository hosRepo;

    //Read All
    public List<Hospital> getAllHospitals(){
        return (List<Hospital>) hosRepo.findAll();
    }

    //Find One
    public Optional<Hospital> getOneHospital(Integer id){
        return hosRepo.findById(id);
    }
}