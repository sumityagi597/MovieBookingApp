package com.cts.MovieBookingApplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.cts.MovieBookingApplication.model.Movie;
import com.cts.MovieBookingApplication.service.MovieService;
import com.cts.MovieBookingApplication.service.TicketService;

@RunWith(MockitoJUnitRunner.class)
class MovieControllerTest {

	@Mock
    MovieService movieService;

    @Mock
    TicketService ticketService;
    
    @InjectMocks
    MovieController controller;
    
    
    @BeforeEach
    void setUp() {
    	MockitoAnnotations.initMocks(this);
    }
	
    @Test
    void testGetAllMovies() {
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
    	when(movieService.getAllMovies()).thenReturn(response);
    	 ResponseEntity<List<Movie>> result = controller.getAllMovies();
    	 Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertEquals("Thor",result.getBody().get(0).getMovieName());
    }
    
    @Test
    void testGetAllMoviesException() {
    	List<Movie> response = new ArrayList<>();
    	response.add(new Movie());
    	when(movieService.getAllMovies()).thenReturn(new ArrayList<>());
    	Assertions.assertThrows(Exception.class, ()->{ 
    		controller.getAllMovies();
    	});
    }

    @Test
    void testGetMovieByName(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieService.getMovieByName(anyString())).thenReturn(response);
        ResponseEntity<List<Movie>> result = controller.getMovieByName("name");
        Assertions.assertEquals("Thor",result.getBody().get(0).getMovieName());
    }

    @Test
    void testGetMovieByNameException() {
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor", "PVR", 120, "ASAP");
        response.add(m);
        when(movieService.getMovieByName(anyString())).thenReturn(new ArrayList<>());
        Assertions.assertThrows(Exception.class,() -> {
            controller.getMovieByName("name");
        });
    }

    @Test
    void testAddMovie(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieService.findByMovieName(anyString())).thenReturn(new ArrayList<>());
        doNothing().when(movieService).saveMovie(any(Movie.class));
        ResponseEntity<String> result = controller.addMovie(m);
        Assertions.assertEquals("Movie added successfully",result.getBody());
    }

    @Test
    void testAddMovie1(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieService.findByMovieName(anyString())).thenReturn(response);
        doNothing().when(movieService).saveMovie(any(Movie.class));
        ResponseEntity<String> result = controller.addMovie(m);
        Assertions.assertEquals("Movie Already Exists!",result.getBody());
    }

    @Test
    void testDeleteMovie(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieService.findByMovieName(anyString())).thenReturn(response);
        doNothing().when(movieService).deleteByMovieName(anyString());
        doNothing().when(ticketService).deleteTicketByMovieName(anyString());
        ResponseEntity<String> result = controller.deleteMovie("Thor");
        Assertions.assertEquals("Movie deleted successfully",result.getBody());
    }

    @Test
    void testDeleteMovie2(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieService.findByMovieName(anyString())).thenReturn(new ArrayList<>());
        doNothing().when(ticketService).deleteTicketByMovieName(anyString());
        doNothing().when(movieService).deleteByMovieName(anyString());
        Assertions.assertThrows(Exception.class,() -> {
            ResponseEntity<String> result = controller.deleteMovie("Thor");
        });
    }
}
