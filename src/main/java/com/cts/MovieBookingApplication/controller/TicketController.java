package com.cts.MovieBookingApplication.controller;

import com.cts.MovieBookingApplication.exception.MoviesNotFound;
import com.cts.MovieBookingApplication.model.Movie;
import com.cts.MovieBookingApplication.exception.SeatAlreadyBooked;
import com.cts.MovieBookingApplication.model.Ticket;
import com.cts.MovieBookingApplication.service.MovieService;
import com.cts.MovieBookingApplication.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1.0/moviebooking")
public class TicketController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private TicketService ticketService;


    @PostMapping("/{movieName}/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> bookTickets(@RequestBody Ticket ticket, @PathVariable String movieName) {
        log.info(ticket.getLoginId() + " entered to book tickets");
        List<Ticket> allTickets = null;
        List<Movie> movie = null;
        try {
             allTickets = ticketService.findSeats(ticket.getMovieName(), ticket.getTheaterName());
             movie = movieService.getMovieByName(ticket.getMovieName());
             if (movie.isEmpty()){
                 throw new Exception("Movie Not Found with Theater");
             }
        }catch (Exception e){
            return new ResponseEntity<>("No Movie Found in Theater with movieName : "+ticket.getMovieName(),HttpStatus.BAD_REQUEST);
        }
        for (Ticket each : allTickets) {
            for (int i = 0; i < ticket.getNoOfTickets(); i++) {
                if (each.getSeatNumber().contains(ticket.getSeatNumber().get(i))) {
                    log.info("seat is already booked");
                    throw new SeatAlreadyBooked("Seat number " + ticket.getSeatNumber().get(i) + " is already booked");
                }
            }
        }
        if(movieService.findAvailableTickets(ticket.getMovieName(),ticket.getTheaterName()).get(0).getNoOfTicketsAvailable() >=
                ticket.getNoOfTickets()){

            log.info("available tickets "
                    +movieService.findAvailableTickets(ticket.getMovieName(),ticket.getTheaterName()).get(0).getNoOfTicketsAvailable());
            ticketService.saveTicket(ticket);
            log.info(ticket.getLoginId()+" booked "+ticket.getNoOfTickets()+" tickets");

            updateAvailableTickectsInMovie(ticket.getMovieName(),ticket.getTheaterName(),ticket.getNoOfTickets(),movie.get(0).getTicketStatus());
            return new ResponseEntity<>("Tickets Booked Successfully with seat numbers"+ticket.getSeatNumber(), HttpStatus.OK);
        }
        else{
            log.info("tickets sold out");
            return new ResponseEntity<>("\"All tickets sold out\"",HttpStatus.OK);
        }
    }

    @GetMapping("/getallbookedtickets/{movieName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Ticket>> getAllBookedTickets(@PathVariable String movieName){
        return new ResponseEntity<>(ticketService.getAllBookedTickets(movieName),HttpStatus.OK);
    }

    private void updateAvailableTickectsInMovie(String moviename,String theatreName,Integer noOfTickets,String ticketStatus) {
        ObjectId objectId = movieService.findAvailableTickets(moviename,theatreName).get(0).get_id();
        Movie movie = new Movie(
                objectId,
                moviename,
                theatreName,
                movieService.findAvailableTickets(moviename,theatreName).get(0).getNoOfTicketsAvailable() - noOfTickets,ticketStatus
        );
        movieService.saveMovie(movie);
    }

    @PutMapping("/{movieName}/update/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateTicketStatus(@PathVariable String movieName, @PathVariable ObjectId ticketId) {
        List<Movie> movie = movieService.findByMovieName(movieName);

        if (movie == null) {
            throw new MoviesNotFound("Movie not found: " + movieName);
        }

        for (Movie movies : movie) {
            if (movies.getNoOfTicketsAvailable() == 0) {
                movies.setTicketStatus("SOLD OUT");
            } else {
                movies.setTicketStatus("BOOK ASAP");
            }
            movieService.saveMovie(movies);
        }
        return new ResponseEntity<>("Ticket status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/tickets/{loginId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTicketByLoginId(@PathVariable String loginId){
        List<Ticket> availableTickets = ticketService.findTicketsByLoginId(loginId);
        if(availableTickets.isEmpty()){
            return new ResponseEntity<>("No Tickets Found!",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(availableTickets,HttpStatus.OK);
        }
    }

}
