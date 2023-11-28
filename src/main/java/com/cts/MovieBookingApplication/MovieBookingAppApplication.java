package com.cts.MovieBookingApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class MovieBookingAppApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(MovieBookingAppApplication.class, args);
	}

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {
	}

}
