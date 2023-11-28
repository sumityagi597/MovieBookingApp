package com.cts.MovieBookingApplication.controller;

import com.cts.MovieBookingApplication.model.ERole;
import com.cts.MovieBookingApplication.model.Role;
import com.cts.MovieBookingApplication.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class CommonController {

    @Autowired
    RoleRepo roleRepo;

    @GetMapping("/health")
    public ResponseEntity<String> getHealth(){
        return new ResponseEntity<>("Microservice Working Fine!!", HttpStatus.OK);
    }

    @GetMapping("/roleChecker")
    public ResponseEntity<String> getRole(){
        List<Role> roles =  roleRepo.findAll();
        if(roles.isEmpty()){
            Role roleAdmin = new Role(ERole.ROLE_ADMIN);
            Role roleUser = new Role(ERole.ROLE_USER);
            Role roleGuest = new Role(ERole.ROLE_GUEST);
            roleRepo.save(roleAdmin);
            roleRepo.save(roleUser);
            roleRepo.save(roleGuest);
            return new ResponseEntity<>("Role Added!!", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Role Already Exist!!", HttpStatus.OK);
        }
    }
}
