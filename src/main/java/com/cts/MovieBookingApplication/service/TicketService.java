package com.cts.MovieBookingApplication.service;

import com.cts.MovieBookingApplication.model.Ticket;
import com.cts.MovieBookingApplication.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepository;

    public List<Ticket> findSeats(String movieName, String theatreName) {
        return ticketRepository.findSeats(movieName,theatreName);
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> getAllBookedTickets(String movieName) {
        return ticketRepository.findByMovieName(movieName);
    }

    public void deleteTicketByMovieName(String movieName){
        ticketRepository.deleteByMovieName(movieName);
    }

    public List<Ticket> findTicketsByLoginId(String loginId) {
        return ticketRepository.findByLoginId(loginId);
    }
}
