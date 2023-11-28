package com.cts.MovieBookingApplication.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.cts.MovieBookingApplication.model.Movie;
import com.cts.MovieBookingApplication.model.Ticket;
import com.cts.MovieBookingApplication.service.MovieService;
import com.cts.MovieBookingApplication.service.TicketService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class TicketControllerTest {

    @Mock
    MovieService movieService;

    @Mock
    TicketService ticketService;

    @InjectMocks
    TicketController controller;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllBookedTickets(){
        List<Ticket> response  = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi","Thor","PVR",2,seat);
        response.add(t);
        when(ticketService.getAllBookedTickets(anyString())).thenReturn(response);
        ResponseEntity<List<Ticket>> result = controller.getAllBookedTickets("Thor");
        Assertions.assertEquals("Thor",result.getBody().get(0).getMovieName());
    }

    @Test
    void testBookTickets(){
        List<Ticket> response  = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi","Thor","PVR",2,seat);
        response.add(t);
        List<Movie> response1 = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response1.add(m);
        when(ticketService.findSeats(anyString(),anyString())).thenReturn(response);
        when(movieService.getMovieByName(anyString())).thenReturn(response1);
        when(movieService.findAvailableTickets(anyString(),anyString())).thenReturn(response1);
        doNothing().when(ticketService).saveTicket(any(Ticket.class));
        doNothing().when(movieService).saveMovie(any(Movie.class));
        List<String> seat1 = new ArrayList<>();
        seat1.add("3");
        seat1.add("4");
        ResponseEntity<String> result = controller.bookTickets(new Ticket("sumityagi","Thor","PVR",2,seat1),"Thor");
        Assertions.assertEquals("Tickets Booked Successfully with seat numbers[3, 4]",result.getBody());
    }

    @Test
    void testBookTickets2(){
        List<Ticket> response  = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi","Thor","PVR",2,seat);
        response.add(t);
        List<Movie> response1 = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response1.add(m);
        when(ticketService.findSeats(anyString(),anyString())).thenReturn(response);
        when(movieService.getMovieByName(anyString())).thenReturn(new ArrayList<>());
        when(movieService.findAvailableTickets(anyString(),anyString())).thenReturn(response1);
        doNothing().when(ticketService).saveTicket(any(Ticket.class));
        doNothing().when(movieService).saveMovie(any(Movie.class));
        List<String> seat1 = new ArrayList<>();
        seat1.add("3");
        seat1.add("4");
        ResponseEntity<String> result = controller.bookTickets(new Ticket("sumityagi","Thor","PVR",2,seat1),"Thor");
        Assertions.assertEquals("No Movie Found in Theater with movieName : Thor",result.getBody());
    }

    @Test
    void testBookTickets3() {
        List<Ticket> response = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi", "Thor", "PVR", 2, seat);
        response.add(t);
        List<Movie> response1 = new ArrayList<>();
        Movie m = new Movie("Thor", "PVR", 120, "ASAP");
        response1.add(m);
        when(ticketService.findSeats(anyString(), anyString())).thenReturn(response);
        when(movieService.getMovieByName(anyString())).thenReturn(response1);
        when(movieService.findAvailableTickets(anyString(), anyString())).thenReturn(response1);
        doNothing().when(ticketService).saveTicket(any(Ticket.class));
        doNothing().when(movieService).saveMovie(any(Movie.class));
        List<String> seat1 = new ArrayList<>();
        seat1.add("3");
        seat1.add("4");
        Assertions.assertThrows(Exception.class,() -> {
            ResponseEntity<String> result = controller.bookTickets(new Ticket("sumityagi", "Thor", "PVR", 2, seat),"Thor");
        });
    }

    @Test
    void testUpdateTicketStatus(){
        List<Movie> response1 = new ArrayList<>();
        Movie m = new Movie("Thor", "PVR", 120, "ASAP");
        response1.add(m);
        when(movieService.findByMovieName(anyString())).thenReturn(response1);
        doNothing().when(movieService).saveMovie(any(Movie.class));
        ResponseEntity<String> result = controller.updateTicketStatus("Thor",new ObjectId("64932cf16b1fb619351db4ec"));
        Assertions.assertEquals("Ticket status updated successfully",result.getBody());
    }
}
