package com.darts.server.repository;

import org.springframework.data.repository.CrudRepository;
import com.darts.server.model.Hospital;

public interface HospitalRepository extends CrudRepository<Hospital,Integer>{
    

}