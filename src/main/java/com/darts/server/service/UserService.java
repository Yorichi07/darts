package com.darts.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darts.server.model.Users;
import com.darts.server.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository usrRepo;

    //Create
    public Users createUsers(Users usr){
        return usrRepo.save(usr);
    }

    //Read All
    public List<Users> getAllUsers(){
        return (List<Users>) usrRepo.findAll();
    }

    //Find One
    public Optional<Users> getOneUsers(Integer id){
        return usrRepo.findById(id);
    }

    //Find By userName
    public Optional<Users> getUserFromUserName(String userName){
        return usrRepo.findByUsername(userName);
    }
}
