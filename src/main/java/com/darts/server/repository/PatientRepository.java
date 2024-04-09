package com.darts.server.repository;

import org.springframework.data.repository.CrudRepository;

import com.darts.server.model.Patient_details;

public interface PatientRepository extends CrudRepository<Patient_details,Integer>{
     
}
