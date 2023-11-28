package com.cts.MovieBookingApplication.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cts.MovieBookingApplication.model.User;

public interface UserRepo extends MongoRepository<User, String> {
	
	Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    Optional<User> findByLoginId(String username);
    Optional<User> findByEmail(String email);

    boolean existsByLoginId(String loginId);
}
