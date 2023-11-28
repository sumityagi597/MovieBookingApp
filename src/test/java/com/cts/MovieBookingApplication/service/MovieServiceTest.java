package com.cts.MovieBookingApplication.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.cts.MovieBookingApplication.model.Movie;
import com.cts.MovieBookingApplication.repository.MovieRepo;
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
class MovieServiceTest {

    @Mock
    MovieRepo movieRepo;

    @InjectMocks
    MovieService movieService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllMovies(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieRepo.findAll()).thenReturn(response);
        List<Movie> result = movieService.getAllMovies();
        Assertions.assertEquals(1,result.size());
    }

    @Test
    void testGetMovieByName(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieRepo.findByMovieName(anyString())).thenReturn(response);
        List<Movie> result = movieService.getMovieByName("Thor");
        Assertions.assertEquals(1,result.size());
    }

    @Test
    void testFindAvailableTickets(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieRepo.findAvailableTickets(anyString(),anyString())).thenReturn(response);
        List<Movie> result = movieService.findAvailableTickets("Thor","PVR");
        Assertions.assertEquals(1,result.size());
    }

    @Test
    void testSaveMovie(){
        when(movieRepo.save(any(Movie.class))).thenReturn(new Movie());
        movieService.saveMovie(new Movie());
        verify(movieRepo,times(1)).save(any(Movie.class));
    }

    @Test
    void testFindByMovieName(){
        List<Movie> response = new ArrayList<>();
        Movie m = new Movie("Thor","PVR",120,"ASAP");
        response.add(m);
        when(movieRepo.findByMovieName(anyString())).thenReturn(response);
        List<Movie> result = movieService.findByMovieName("Thor");
        Assertions.assertEquals(1,result.size());
    }

    @Test
    void testDeleteByMovieName(){
        doNothing().when(movieRepo).deleteByMovieName(anyString());
        movieService.deleteByMovieName("Thor");
        verify(movieRepo,times(1)).deleteByMovieName(anyString());
    }

}
