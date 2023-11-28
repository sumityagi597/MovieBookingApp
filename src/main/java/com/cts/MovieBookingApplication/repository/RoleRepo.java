package com.cts.MovieBookingApplication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cts.MovieBookingApplication.model.ERole;
import com.cts.MovieBookingApplication.model.Role;

public interface RoleRepo extends MongoRepository<Role, String> {
	
	Optional<Role> findByName(ERole name);

}
