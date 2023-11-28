package com.cts.MovieBookingApplication.controller;

import com.cts.MovieBookingApplication.model.Movie;
import com.cts.MovieBookingApplication.exception.MoviesNotFound;
import com.cts.MovieBookingApplication.service.MovieService;
import com.cts.MovieBookingApplication.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v1.0/moviebooking")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies(){
        log.info("Inside /all");
        List<Movie> movieList = movieService.getAllMovies();
        if(movieList.isEmpty()){
            log.info("currently no movies are available");
            throw new MoviesNotFound("No Movies are available");
        }
        else{
            log.info("listed the available movies : "+ movieList.toString());
            return new ResponseEntity<>(movieList, HttpStatus.OK);
        }
    }

    @GetMapping("/movies/search/{movieName}")
    public ResponseEntity<List<Movie>> getMovieByName(@PathVariable String movieName){
        log.info("here search a movie by its name");
        List<Movie> movieList = movieService.getMovieByName(movieName);
        if(movieList.isEmpty()){
            log.info("currently no movies are available");
            throw new MoviesNotFound("Movies Not Found");
        }
        else
            log.info("listed the available movies with title:"+movieName);
        return new ResponseEntity<>(movieList,HttpStatus.OK);
    }

    @PostMapping("/addMovie")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addMovie(@RequestBody Movie movie){
        List<Movie> availableMovies = movieService.findByMovieName(movie.getMovieName());
        if(availableMovies.isEmpty()){
            movieService.saveMovie(movie);
            return new ResponseEntity<>("Movie added successfully",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Movie Already Exists!",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{movieName}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieName){
        List<Movie> availableMovies = movieService.findByMovieName(movieName);
        if(availableMovies.isEmpty()){
            throw new MoviesNotFound("No movies Available with moviename "+ movieName);
        }
        else {
            movieService.deleteByMovieName(movieName);
            ticketService.deleteTicketByMovieName(movieName);
            return new ResponseEntity<>("Movie deleted successfully",HttpStatus.OK);
        }
    }

    @PutMapping("/{movieName}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateMovie(@PathVariable String movieName,@RequestBody Movie movie){
        List<Movie> availableMovies = movieService.findByMovieName(movieName);
        if(availableMovies.isEmpty()){
            throw new MoviesNotFound("No movies Available with moviename "+ movieName);
        }
        else {
            movieService.deleteByMovieName(movieName);
            movieService.saveMovie(movie);
            return new ResponseEntity<>("Movie updated successfully",HttpStatus.OK);
        }
    }
}
