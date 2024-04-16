package com.darts.server.repository;

import java.util.Optional;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.darts.server.model.Specialist;

public interface SpecialistRepository extends CrudRepository<Specialist,Integer>{
    //to find one doctor by name
    public Optional<Specialist> findByDocName(String docName);    
    
    //to find one doctor of a particular speciality
    public Optional<Specialist> findBySpeciality(String speciality);
    
    //to find all doctors of paticular speciality
    public List<Specialist> findAllBySpeciality(String speciality);

    //to find distinct specialities in the table
    @Query("SELECT DISTINCT s.speciality FROM Specialist s")
    public Set<String> findDistinctBySpeciality();  
    
    //find doctor by userName
    public Optional<Specialist> findByUsername(String userName);
}