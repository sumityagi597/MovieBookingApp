package com.cts.MovieBookingApplication.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "movie")
@Data
@NoArgsConstructor
public class Movie {
	
	private ObjectId _id;
    private String movieName;

    private String theaterName;
    private Integer noOfTicketsAvailable;
    private String ticketStatus;

    public Movie(String movieName, String theaterName, Integer noOfTicketsAvailable, String ticketsStatus) {
        this.movieName = movieName;
        this.theaterName = theaterName;
        this.noOfTicketsAvailable = noOfTicketsAvailable;
        this.ticketStatus = ticketsStatus;
    }

    public Movie(ObjectId _id, String movieName, String theaterName, Integer noOfTicketsAvailable,String ticketStatus) {
        this._id = _id;
        this.movieName = movieName;
        this.theaterName = theaterName;
        this.noOfTicketsAvailable = noOfTicketsAvailable;
        this.ticketStatus = ticketStatus;
    }
}
