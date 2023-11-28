package com.cts.MovieBookingApplication.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.cts.MovieBookingApplication.model.Ticket;
import com.cts.MovieBookingApplication.repository.TicketRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class TicketServiceTest {

    @Mock
    TicketRepo ticketRepo;

    @InjectMocks
    TicketService service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindSeats(){
        List<Ticket> response  = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi","Thor","PVR",2,seat);
        response.add(t);
        when(ticketRepo.findSeats(anyString(),anyString())).thenReturn(response);
        List<Ticket> result = service.findSeats("Thor","PVR");
        Assertions.assertEquals("Thor",result.get(0).getMovieName());
    }

    @Test
    void testSaveTicket(){
        when(ticketRepo.save(any(Ticket.class))).thenReturn(new Ticket());
        service.saveTicket(new Ticket());
        verify(ticketRepo,times(1)).save(any(Ticket.class));
    }

    @Test
    void testGetAllBookedTickets(){
        List<Ticket> response  = new ArrayList<>();
        List<String> seat = new ArrayList<>();
        seat.add("1");
        seat.add("2");
        Ticket t = new Ticket("sumityagi","Thor","PVR",2,seat);
        response.add(t);
        when(ticketRepo.findByMovieName(anyString())).thenReturn(response);
        List<Ticket> result = service.getAllBookedTickets("Thor");
        Assertions.assertEquals("Thor",result.get(0).getMovieName());
    }
}
