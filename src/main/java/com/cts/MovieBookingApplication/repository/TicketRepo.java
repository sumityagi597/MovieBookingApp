package com.cts.MovieBookingApplication.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cts.MovieBookingApplication.model.Ticket;

public interface TicketRepo extends MongoRepository<Ticket, String> {
	
	@Query(value = "{'movieName': ?0, 'theaterName': ?1}", fields = "{_id:0, seatNumber:1}")
	List<Ticket> findSeats(String movieName, String theaterName);
	
	List<Ticket> findByMovieName(String movieName);

    List<Ticket> findBy_id(ObjectId _id);

	List<Ticket> findByLoginId(String loginId);

	void deleteByMovieName(String movieName);
}
