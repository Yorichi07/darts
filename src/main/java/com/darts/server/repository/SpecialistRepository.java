package com.darts.server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.darts.server.model.Specialist;

public interface SpecialistRepository extends CrudRepository<Specialist,Integer>{
    public Optional<Specialist> findByDocName(String docName);    
    public Optional<Specialist> findBySpeciality(String speciality);
}