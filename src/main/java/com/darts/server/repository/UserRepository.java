package com.darts.server.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.darts.server.model.Users;

public interface UserRepository extends CrudRepository<Users,Integer>{

    public Optional<Users> findByUsername(String userName);     //to get from non-primary key parameter

}