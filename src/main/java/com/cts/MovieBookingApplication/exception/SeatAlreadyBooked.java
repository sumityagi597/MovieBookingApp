package com.cts.MovieBookingApplication.exception;

public class SeatAlreadyBooked extends RuntimeException {
	public SeatAlreadyBooked(String s) {
		super(s);
	}
}