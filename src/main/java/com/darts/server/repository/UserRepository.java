package com.darts.server.repository;

import org.springframework.data.repository.CrudRepository;
import com.darts.server.model.Users;

public interface UserRepository extends CrudRepository<Users,Long>{
    

}